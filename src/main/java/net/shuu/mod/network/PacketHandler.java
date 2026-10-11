package net.shuu.mod.network;

import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.shuu.mod.AutomateTradeCycling;
import net.shuu.mod.mixin.MerchantScreenHandlerAccessor;
import net.shuu.mod.mixin.VillagerTradeAccessor;

/**
 * Handler untuk memproses cycling di server side
 * Berdasarkan implementasi dari trade-cycling mod
 */
public class PacketHandler {

  /** Open the vanilla merchant menu for the villager selected on the client. */
  public static void openVillagerTrade(ServerPlayer player, int entityId) {
    if (player == null) return;

    net.minecraft.world.entity.Entity entity = player.level().getEntity(entityId);
    if (!(entity instanceof Villager villager)
        || !villager.isAlive()
        || player.distanceToSqr(villager) > 64.0) {
      AutomateTradeCycling.LOGGER.warn("Rejected villager trade open request: invalid or out-of-range villager");
      return;
    }

    // Avoid treating a villager spawn egg as a normal trade interaction.
    if (player.getItemInHand(InteractionHand.MAIN_HAND).is(net.minecraft.world.item.Items.VILLAGER_SPAWN_EGG)) {
      player.sendOverlayMessage(net.minecraft.network.chat.Component.literal(
          "Lepas villager spawn egg dari tangan utama sebelum memulai cycling."));
      return;
    }

    AutomateTradeCycling.LOGGER.info("Opening merchant screen for selected villager: "
        + villager.getVillagerData().profession().unwrapKey().orElse(VillagerProfession.NONE));
    villager.mobInteract(player, InteractionHand.MAIN_HAND);
  }

  /**
   * Handle cycling request dari client
   * Method ini akan:
   * 1. Validasi bahwa player sedang membuka merchant screen
   * 2. Regenerate trade offers villager
   * 3. Update trade offers ke client
   */
  public static void onCycleTrades(ServerPlayer player) {
    if (player == null) {
      return;
    }

    // Dapatkan screen handler yang sedang dibuka player
    AbstractContainerMenu screenHandler = player.containerMenu;
    if (!(screenHandler instanceof MerchantMenu merchantAbstractContainerMenu)) {
      AutomateTradeCycling.LOGGER.warn("Player tidak membuka merchant screen");
      return;
    }

    // Gunakan accessor untuk mendapatkan merchant
    if (!(screenHandler instanceof MerchantScreenHandlerAccessor accessor)) {
      AutomateTradeCycling.LOGGER.warn("Cannot access merchant from screen handler");
      return;
    }

    Merchant merchant = accessor.getMerchant();

    // Validasi bahwa merchant adalah villager yang belum pernah di-trade
    if (!(merchant instanceof Villager villager)) {
      AutomateTradeCycling.LOGGER.warn("Merchant bukan villager");
      return;
    }

    // Cek apakah villager sudah pernah di-trade (ada XP)
    if (villager.getVillagerXp() > 0) {
      AutomateTradeCycling.LOGGER.info("Villager sudah pernah di-trade (XP: " + villager.getVillagerXp() + ")");
      return;
    }

    // Cek apakah villager punya job site
    if (!villager.getBrain().hasMemoryValue(net.minecraft.world.entity.ai.memory.MemoryModuleType.JOB_SITE)) {
      AutomateTradeCycling.LOGGER.warn("Villager tidak punya job site");
      return;
    }

    AutomateTradeCycling.LOGGER.info("Regenerating trades for villager: " + villager.getVillagerData().profession().unwrapKey().orElse(VillagerProfession.NONE));

    // CORE LOGIC: Regenerate trade offers
    // Ini adalah cara yang digunakan oleh trade-cycling mod
    // getOffers() only generates trades when its offers field is null. Setting
    // it to an empty list and calling getOffers() therefore leaves it empty.
    // Clear the list, then invoke vanilla's trade generation directly.
    villager.setOffers(new MerchantOffers());
    ((VillagerTradeAccessor) villager).invokeUpdateTrades(
        (net.minecraft.server.level.ServerLevel) villager.level());

    // Kirim updated offers ke client
    MerchantOffers offers = villager.getOffers();
    int villagerLevel = villager.getVillagerData().level();
    int experience = villager.getVillagerXp();
    boolean canRestock = villager.canRestock();

    player.sendMerchantOffers(
        merchantAbstractContainerMenu.containerId,
        offers,
        villagerLevel,
        experience,
        false, // leveling - always false for cycling (we only cycle level 1 villagers)
        canRestock);

    AutomateTradeCycling.LOGGER.info("Successfully cycled trades. New offers: " + offers.size());
  }
}

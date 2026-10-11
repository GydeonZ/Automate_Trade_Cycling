package net.shuu.mod.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;
import net.shuu.mod.AutomateTradeCycling;
import net.shuu.mod.mixin.MerchantScreenHandlerAccessor;

/**
 * Handler untuk memproses cycling di server side
 * Berdasarkan implementasi dari trade-cycling mod
 */
public class PacketHandler {

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
    if (!(screenHandler instanceof MerchantMenu merchantMenu)) {
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
    if (villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).isEmpty()) {
      AutomateTradeCycling.LOGGER.warn("Villager tidak punya job site");
      return;
    }

    AutomateTradeCycling.LOGGER
        .info("Regenerating trades for villager: " + villager.getVillagerData().profession());

    // CORE LOGIC: Regenerate trade offers
    // Ini adalah cara yang digunakan oleh trade-cycling mod:
    // menghapus offers lama, lalu getOffers() akan membangun ulang
    // (updateTrades) secara otomatis.
    villager.setOffers(null);

    MerchantOffers offers = villager.getOffers();
    int villagerLevel = villager.getVillagerData().level();
    int experience = villager.getVillagerXp();
    boolean canRestock = villager.canRestock();

    player.sendMerchantOffers(
        merchantMenu.containerId,
        offers,
        villagerLevel,
        experience,
        false, // leveling - always false for cycling (we only cycle level 1 villagers)
        canRestock);

    AutomateTradeCycling.LOGGER.info("Successfully cycled trades. New offers: " + offers.size());
  }
}

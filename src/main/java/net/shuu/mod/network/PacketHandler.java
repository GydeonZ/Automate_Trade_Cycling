package net.shuu.mod.network;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOfferList;
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
  public static void onCycleTrades(ServerPlayerEntity player) {
    if (player == null) {
      return;
    }

    // Dapatkan screen handler yang sedang dibuka player
    ScreenHandler screenHandler = player.currentScreenHandler;
    if (!(screenHandler instanceof MerchantScreenHandler merchantScreenHandler)) {
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
    if (!(merchant instanceof VillagerEntity villager)) {
      AutomateTradeCycling.LOGGER.warn("Merchant bukan villager");
      return;
    }

    // Cek apakah villager sudah pernah di-trade (ada XP)
    if (villager.getExperience() > 0) {
      AutomateTradeCycling.LOGGER.info("Villager sudah pernah di-trade (XP: " + villager.getExperience() + ")");
      return;
    }

    // Cek apakah villager punya job site
    if (villager.getBrain().getOptionalMemory(net.minecraft.entity.ai.brain.MemoryModuleType.JOB_SITE).isEmpty()) {
      AutomateTradeCycling.LOGGER.warn("Villager tidak punya job site");
      return;
    }

    AutomateTradeCycling.LOGGER.info("Regenerating trades for villager: " + villager.getVillagerData().getProfession());

    // CORE LOGIC: Regenerate trade offers
    // Ini adalah cara yang digunakan oleh trade-cycling mod
    villager.setOffers(null); // Clear offers
    villager.getOffers(); // Regenerate new offers

    // Note: prepareOffersFor is private in Minecraft 1.21.1
    // We don't need to call it because getOffers() already handles special prices

    // Kirim updated offers ke client
    TradeOfferList offers = villager.getOffers();
    int villagerLevel = villager.getVillagerData().getLevel();
    int experience = villager.getExperience();
    boolean canRestock = villager.canRefreshTrades();

    player.sendTradeOffers(
        merchantScreenHandler.syncId,
        offers,
        villagerLevel,
        experience,
        false, // leveling - always false for cycling (we only cycle level 1 villagers)
        canRestock);

    AutomateTradeCycling.LOGGER.info("Successfully cycled trades. New offers: " + offers.size());
  }
}

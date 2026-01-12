package net.shuu.mod.manager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerProfession;
import net.shuu.mod.AutomateTradeCycling;
import net.shuu.mod.config.TradeCyclingConfig;
import net.shuu.mod.util.VillagerProfessionHelper;

import java.util.Set;

public class TradeCyclingManager {
  private static TradeCyclingManager INSTANCE;

  private VillagerEntity targetVillager = null;
  private boolean isSearching = false;
  private int cycleDelay = 0;
  private static final int CYCLE_DELAY_TICKS = 10; // 0.5 detik

  public static TradeCyclingManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TradeCyclingManager();
    }
    return INSTANCE;
  }

  private TradeCyclingManager() {
  }

  public void startCycling(VillagerEntity villager) {
    if (villager == null) {
      AutomateTradeCycling.LOGGER.warn("Cannot start cycling: villager is null");
      return;
    }

    VillagerProfession profession = villager.getVillagerData().getProfession();
    TradeCyclingConfig config = TradeCyclingConfig.getInstance();

    if (!config.hasDesiredItems(profession)) {
      MinecraftClient client = MinecraftClient.getInstance();
      if (client.player != null) {
        client.player.sendMessage(Text.literal("§cTidak ada item yang dicari untuk profesi " +
            VillagerProfessionHelper.getProfessionDisplayName(profession) + "!"), false);
      }
      return;
    }

    this.targetVillager = villager;
    this.isSearching = true;
    config.setCycling(true);
    config.setCurrentProfession(profession);

    MinecraftClient client = MinecraftClient.getInstance();
    if (client.player != null) {
      Set<Item> desiredItems = config.getDesiredItems(profession);
      client.player.sendMessage(Text.literal("§aMemulai pencarian trade untuk " +
          desiredItems.size() + " item..."), false);
    }

    AutomateTradeCycling.LOGGER.info("Started trade cycling for profession: " + profession);
  }

  public void stopCycling() {
    this.isSearching = false;
    this.targetVillager = null;
    TradeCyclingConfig.getInstance().setCycling(false);
    TradeCyclingConfig.getInstance().setCurrentProfession(null);

    AutomateTradeCycling.LOGGER.info("Stopped trade cycling");
  }

  public void tick(MinecraftClient client) {
    if (!isSearching || targetVillager == null || client.player == null) {
      return;
    }

    // Cek apakah villager masih valid
    if (!targetVillager.isAlive() || targetVillager.isRemoved()) {
      stopCycling();
      client.player.sendMessage(Text.literal("§cVillager hilang atau mati!"), false);
      return;
    }

    // Delay untuk cycling
    if (cycleDelay > 0) {
      cycleDelay--;
      return;
    }

    // Cek trade offers
    if (checkTradeOffers(client.player)) {
      stopCycling();
      return;
    }

    // Lakukan cycling dengan memanggil jobsite
    cycleVillagerTrades(client.player);
    cycleDelay = CYCLE_DELAY_TICKS;
  }

  private boolean checkTradeOffers(ClientPlayerEntity player) {
    if (targetVillager == null)
      return false;

    TradeOfferList offers = targetVillager.getOffers();
    if (offers == null || offers.isEmpty()) {
      return false;
    }

    VillagerProfession profession = targetVillager.getVillagerData().getProfession();
    Set<Item> desiredItems = TradeCyclingConfig.getInstance().getDesiredItems(profession);

    if (desiredItems.isEmpty()) {
      return false;
    }

    // Cek apakah ada item yang diinginkan di trade offers
    for (TradeOffer offer : offers) {
      Item sellItem = offer.getSellItem().getItem();

      if (desiredItems.contains(sellItem)) {
        player.sendMessage(Text.literal("§a§l[DITEMUKAN!] §r§a" +
            sellItem.getName().getString() + " tersedia untuk trade!"), false);
        AutomateTradeCycling.LOGGER.info("Found desired item: " + sellItem.getName().getString());
        return true;
      }
    }

    return false;
  }

  private void cycleVillagerTrades(ClientPlayerEntity player) {
    if (targetVillager == null)
      return;

    // Untuk cycle trade, kita perlu break jobsite dan letakkan lagi
    // Ini simulasi - di implementasi sebenarnya perlu interact dengan jobsite block
    // Untuk sementara, kita hanya log cycling

    VillagerProfession profession = targetVillager.getVillagerData().getProfession();
    AutomateTradeCycling.LOGGER.debug("Cycling trades for villager with profession: " + profession);

    // Di sini seharusnya ada logic untuk:
    // 1. Cari jobsite block terdekat
    // 2. Break jobsite
    // 3. Place jobsite kembali
    // 4. Tunggu villager refresh trades

    // Note: fillRecipes() is protected, trade cycling akan terjadi saat
    // villager interact dengan jobsite block secara natural
  }

  public boolean isSearching() {
    return isSearching;
  }

  public VillagerEntity getTargetVillager() {
    return targetVillager;
  }

  // Helper untuk mendapatkan villager yang sedang dilihat player
  public static VillagerEntity getTargetedVillager(MinecraftClient client) {
    if (client.player == null || client.crosshairTarget == null) {
      return null;
    }

    HitResult hitResult = client.crosshairTarget;
    if (hitResult.getType() == HitResult.Type.ENTITY) {
      EntityHitResult entityHit = (EntityHitResult) hitResult;
      if (entityHit.getEntity() instanceof VillagerEntity) {
        return (VillagerEntity) entityHit.getEntity();
      }
    }

    return null;
  }
}

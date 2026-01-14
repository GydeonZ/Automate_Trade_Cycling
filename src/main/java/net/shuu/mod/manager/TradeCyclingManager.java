package net.shuu.mod.manager;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerProfession;
import net.shuu.mod.AutomateTradeCycling;
import net.shuu.mod.config.EnchantmentWithLevel;
import net.shuu.mod.config.TradeCyclingConfig;
import net.shuu.mod.network.CycleTradesPacket;
import net.shuu.mod.util.VillagerProfessionHelper;

import java.util.Set;

public class TradeCyclingManager {
  private static TradeCyclingManager INSTANCE;

  private VillagerEntity targetVillager = null;
  private boolean isSearching = false;
  private boolean foundDesiredItem = false;
  private int cycleDelay = 0;
  private int cyclingDelayMs = 1000; // Default 1 second (1000ms)

  public static TradeCyclingManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TradeCyclingManager();
    }
    return INSTANCE;
  }

  private TradeCyclingManager() {
  }

  public int getCyclingDelayMs() {
    return cyclingDelayMs;
  }

  public void setCyclingDelayMs(int ms) {
    this.cyclingDelayMs = Math.max(50, Math.min(10000, ms)); // Clamp between 50-10000ms
    AutomateTradeCycling.LOGGER.info("Cycling speed updated to: " + ms + "ms");
  }

  private int getCyclingDelayTicks() {
    return cyclingDelayMs / 50; // Convert ms to ticks (1000ms = 20 ticks)
  }

  public void startCycling(VillagerEntity villager) {
    if (villager == null) {
      AutomateTradeCycling.LOGGER.warn("Cannot start cycling: villager is null");
      return;
    }

    VillagerProfession profession = villager.getVillagerData().getProfession();
    TradeCyclingConfig config = TradeCyclingConfig.getInstance();

    // Check if it's Librarian with enchantments OR other profession with items
    boolean hasDesiredItems = config.hasDesiredItems(profession);
    boolean hasDesiredEnchantments = config.hasDesiredEnchantments(profession);

    if (!hasDesiredItems && !hasDesiredEnchantments) {
      MinecraftClient client = MinecraftClient.getInstance();
      if (client.player != null) {
        client.player.sendMessage(Text.literal("§cTidak ada item/enchantment yang dicari untuk profesi " +
            VillagerProfessionHelper.getProfessionDisplayName(profession) + "!"), false);
      }
      return;
    }

    this.targetVillager = villager;
    this.isSearching = true;
    this.foundDesiredItem = false;
    config.setCycling(true);
    config.setCurrentProfession(profession);

    MinecraftClient client = MinecraftClient.getInstance();
    if (client.player != null) {
      if (hasDesiredEnchantments) {
        // Librarian with enchantments
        int enchantCount = config.getDesiredEnchantments(profession).size();
        client.player.sendMessage(Text.literal("§a§l[CYCLING AKTIF]"), false);
        client.player.sendMessage(Text.literal("§eMencari " + enchantCount + " enchantment untuk " +
            VillagerProfessionHelper.getProfessionDisplayName(profession)), false);
      } else {
        // Other professions with items
        Set<Item> desiredItems = config.getDesiredItems(profession);
        client.player.sendMessage(Text.literal("§a§l[CYCLING AKTIF]"), false);
        client.player.sendMessage(Text.literal("§eMencari " + desiredItems.size() + " item untuk " +
            VillagerProfessionHelper.getProfessionDisplayName(profession)), false);
      }
      client.player.sendMessage(Text.literal("§7Klik kanan villager untuk memulai..."), false);
    }

    AutomateTradeCycling.LOGGER.info("Started trade cycling for profession: " + profession);
  }

  public void stopCycling() {
    AutomateTradeCycling.LOGGER.info(
        "stopCycling() called - isSearching: " + this.isSearching + ", foundDesiredItem: " + this.foundDesiredItem);
    this.isSearching = false;
    this.targetVillager = null;
    this.foundDesiredItem = false;
    this.cycleDelay = 0;
    TradeCyclingConfig.getInstance().setCycling(false);
    TradeCyclingConfig.getInstance().setCurrentProfession(null);

    AutomateTradeCycling.LOGGER.info("Trade cycling stopped - all flags reset");
  }

  public void tick(MinecraftClient client) {
    if (!isSearching || targetVillager == null || client.player == null) {
      return;
    }

    // Jika sudah menemukan item yang diinginkan, stop cycling
    if (foundDesiredItem) {
      AutomateTradeCycling.LOGGER.info("[TICK] foundDesiredItem=true, calling stopCycling()");
      stopCycling();
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

    // Cek apakah player sedang membuka merchant screen dengan target villager
    if (client.player.currentScreenHandler instanceof MerchantScreenHandler merchantHandler) {
      // Kirim packet cycling ke server
      AutomateTradeCycling.LOGGER
          .info("[TICK] Sending cycle packet, isSearching: " + isSearching + ", foundDesiredItem: " + foundDesiredItem
              + ", delayMs: " + cyclingDelayMs);
      sendCyclePacket();
      cycleDelay = getCyclingDelayTicks();
    }
  }

  /**
   * Kirim packet cycling ke server
   */
  private void sendCyclePacket() {
    if (ClientPlayNetworking.canSend(CycleTradesPacket.ID)) {
      ClientPlayNetworking.send(new CycleTradesPacket());
      AutomateTradeCycling.LOGGER.info("Sent cycle trades packet to server");
    } else {
      AutomateTradeCycling.LOGGER.warn("Cannot send cycle trades packet - server doesn't support it");
    }
  }

  /**
   * Method untuk check offers ketika merchant screen dibuka
   * Dipanggil dari MerchantScreenMixin
   */
  public boolean checkCurrentOffers(TradeOfferList offers) {
    if (!isSearching || targetVillager == null) {
      return false;
    }

    MinecraftClient client = MinecraftClient.getInstance();
    if (client.player == null || client.world == null) {
      return false;
    }

    if (offers == null || offers.isEmpty()) {
      return false;
    }

    VillagerProfession profession = targetVillager.getVillagerData().getProfession();
    TradeCyclingConfig config = TradeCyclingConfig.getInstance();

    // Check if we're looking for enchantments (Librarian) or regular items
    Set<EnchantmentWithLevel> desiredEnchantments = config.getDesiredEnchantments(profession);
    Set<Item> desiredItems = config.getDesiredItems(profession);

    if (desiredEnchantments.isEmpty() && desiredItems.isEmpty()) {
      return false;
    }

    // Cek apakah ada item/enchantment yang diinginkan di trade offers
    for (TradeOffer offer : offers) {
      // Safety check untuk offer yang valid
      if (offer == null || offer.isDisabled() || offer.getSellItem() == null) {
        continue;
      }

      Item sellItem = offer.getSellItem().getItem();

      // Safety check untuk item yang valid
      if (sellItem == null) {
        continue;
      }

      // Check for enchanted books with specific enchantments
      if (sellItem == Items.ENCHANTED_BOOK && !desiredEnchantments.isEmpty()) {
        ItemEnchantmentsComponent enchantments = offer.getSellItem().get(DataComponentTypes.STORED_ENCHANTMENTS);

        if (enchantments != null) {
          for (RegistryEntry<Enchantment> enchantmentEntry : enchantments.getEnchantments()) {
            // Get enchantment level from the book
            int level = enchantments.getLevel(enchantmentEntry);

            // Check if this enchantment matches our filter
            if (enchantmentEntry.getKey().isPresent()) {
              RegistryKey<Enchantment> enchantmentKey = enchantmentEntry.getKey().get();

              for (EnchantmentWithLevel desired : desiredEnchantments) {
                if (desired.getEnchantment().equals(enchantmentKey) && desired.matchesLevel(level)) {
                  // FOUND enchantment with matching level!
                  String enchantName;
                  try {
                    enchantName = Enchantment.getName(enchantmentEntry, level).getString();
                  } catch (Exception e) {
                    enchantName = "Enchanted Book";
                    AutomateTradeCycling.LOGGER.warn("Failed to get enchantment name: " + e.getMessage());
                  }

                  AutomateTradeCycling.LOGGER
                      .info("[CHECK_OFFERS] MATCH FOUND! Enchantment: " + enchantName + ", Level: " + level);
                  AutomateTradeCycling.LOGGER
                      .info("[CHECK_OFFERS] Setting foundDesiredItem=true and calling stopCycling()");

                  // Stop cycling immediately
                  this.foundDesiredItem = true;
                  stopCycling();

                  client.player.sendMessage(Text.literal("§a§l========================================"), false);
                  client.player.sendMessage(Text.literal("§a§l[DITEMUKAN!] §r§a" + enchantName), false);
                  client.player.sendMessage(Text.literal("§a§l========================================"), false);
                  client.player.sendMessage(Text.literal("§eEnchantment tersedia untuk trade!"), false);
                  client.player.sendMessage(Text.literal("§7Cycling dihentikan."), false);

                  AutomateTradeCycling.LOGGER.info("[CHECK_OFFERS] Returning true, cycling should be stopped now");
                  return true;
                }
              }
            }
          }
        }
      }

      // Check for regular items
      if (!desiredItems.isEmpty() && desiredItems.contains(sellItem)) {
        // Dapatkan nama item dengan aman
        String itemName;
        try {
          itemName = offer.getSellItem().getName().getString();
        } catch (Exception e) {
          itemName = "Unknown Item";
          AutomateTradeCycling.LOGGER.warn("Failed to get item name: " + e.getMessage());
        }

        client.player.sendMessage(Text.literal("§a§l========================================"), false);
        client.player.sendMessage(Text.literal("§a§l[DITEMUKAN!] §r§a" + itemName), false);
        client.player.sendMessage(Text.literal("§a§l========================================"), false);
        client.player.sendMessage(Text.literal("§eItem tersedia untuk trade!"), false);
        client.player.sendMessage(Text.literal("§7Cycling dihentikan."), false);

        AutomateTradeCycling.LOGGER.info("Found desired item: " + itemName);

        // Stop cycling immediately
        this.foundDesiredItem = true;
        stopCycling();
        return true;
      }
    }

    return false;
  }

  public boolean isSearching() {
    return isSearching;
  }

  public boolean hasFoundDesiredItem() {
    return foundDesiredItem;
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

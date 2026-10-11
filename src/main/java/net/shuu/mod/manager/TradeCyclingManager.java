package net.shuu.mod.manager;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.shuu.mod.AutomateTradeCycling;
import net.shuu.mod.config.EnchantmentWithLevel;
import net.shuu.mod.config.TradeCyclingConfig;
import net.shuu.mod.network.CycleTradesPacket;
import net.shuu.mod.network.OpenVillagerTradePacket;
import net.shuu.mod.util.VillagerProfessionHelper;

import java.util.Set;

public class TradeCyclingManager {
  private static TradeCyclingManager INSTANCE;

  private Villager targetVillager = null;
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

  public void startCycling(Villager villager) {
    if (villager == null) {
      AutomateTradeCycling.LOGGER.warn("Cannot start cycling: villager is null");
      return;
    }

    ResourceKey<VillagerProfession> profession = villager.getVillagerData().profession().unwrapKey().orElse(VillagerProfession.NONE);
    TradeCyclingConfig config = TradeCyclingConfig.getInstance();

    // Check if it's Librarian with enchantments OR other profession with items
    boolean hasDesiredItems = config.hasDesiredItems(profession);
    boolean hasDesiredEnchantments = config.hasDesiredEnchantments(profession);

    if (!hasDesiredItems && !hasDesiredEnchantments) {
      Minecraft client = Minecraft.getInstance();
      if (client.player != null) {
        client.player.sendOverlayMessage(Component.literal("§cTidak ada item/enchantment yang dicari untuk profesi " +
            VillagerProfessionHelper.getProfessionDisplayName(profession) + "!"));
      }
      return;
    }

    this.targetVillager = villager;
    this.isSearching = true;
    this.foundDesiredItem = false;
    config.setCycling(true);
    config.setCurrentProfession(profession);

    Minecraft client = Minecraft.getInstance();
    if (client.player != null) {
      if (hasDesiredEnchantments) {
        // Librarian with enchantments
        int enchantCount = config.getDesiredEnchantments(profession).size();
        client.player.sendOverlayMessage(Component.literal("§a§l[CYCLING AKTIF]"));
        client.player.sendOverlayMessage(Component.literal("§eMencari " + enchantCount + " enchantment untuk " +
            VillagerProfessionHelper.getProfessionDisplayName(profession)));
      } else {
        // Other professions with items
        Set<Item> desiredItems = config.getDesiredItems(profession);
        client.player.sendOverlayMessage(Component.literal("§a§l[CYCLING AKTIF]"));
        client.player.sendOverlayMessage(Component.literal("§eMencari " + desiredItems.size() + " item untuk " +
            VillagerProfessionHelper.getProfessionDisplayName(profession)));
      }
      client.player.sendOverlayMessage(Component.literal("§7Cycling berjalan saat layar trade villager terbuka."));
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

  public void tick(Minecraft client) {
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
      client.player.sendOverlayMessage(Component.literal("§cVillager hilang atau mati!"));
      return;
    }

    // Delay untuk cycling
    if (cycleDelay > 0) {
      cycleDelay--;
      return;
    }

    // Cek apakah player sedang membuka merchant screen dengan target villager
    if (client.player.containerMenu instanceof MerchantMenu merchantHandler) {
      // Kirim packet cycling ke server
      AutomateTradeCycling.LOGGER
          .info("[TICK] Sending cycle packet, isSearching: " + isSearching + ", foundDesiredItem: " + foundDesiredItem
              + ", delayMs: " + cyclingDelayMs);
      sendCyclePacket();
      cycleDelay = getCyclingDelayTicks();
    }
  }

  /** Ask the server to open the selected villager's vanilla merchant screen. */
  public void openTradeScreenAfterSelection(Villager villager) {
    if (villager != null && ClientPlayNetworking.canSend(OpenVillagerTradePacket.ID)) {
      ClientPlayNetworking.send(new OpenVillagerTradePacket(villager.getId()));
      AutomateTradeCycling.LOGGER.info("Sent request to open villager merchant screen");
    } else {
      AutomateTradeCycling.LOGGER.warn("Cannot request villager merchant screen from server");
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
  public boolean checkCurrentOffers(MerchantOffers offers) {
    if (!isSearching || targetVillager == null) {
      return false;
    }

    Minecraft client = Minecraft.getInstance();
    if (client.player == null || client.level == null) {
      return false;
    }

    if (offers == null || offers.isEmpty()) {
      return false;
    }

    ResourceKey<VillagerProfession> profession = targetVillager.getVillagerData().profession().unwrapKey().orElse(VillagerProfession.NONE);
    TradeCyclingConfig config = TradeCyclingConfig.getInstance();

    // Check if we're looking for enchantments (Librarian) or regular items
    Set<EnchantmentWithLevel> desiredEnchantments = config.getDesiredEnchantments(profession);
    Set<Item> desiredItems = config.getDesiredItems(profession);

    if (desiredEnchantments.isEmpty() && desiredItems.isEmpty()) {
      return false;
    }

    // Cek apakah ada item/enchantment yang diinginkan di trade offers
    for (MerchantOffer offer : offers) {
      // Safety check untuk offer yang valid
      if (offer == null || offer.isOutOfStock() || offer.getResult() == null) {
        continue;
      }

      Item sellItem = offer.getResult().getItem();

      // Safety check untuk item yang valid
      if (sellItem == null) {
        continue;
      }

      // Check for enchanted books with specific enchantments
      if (sellItem == Items.ENCHANTED_BOOK && !desiredEnchantments.isEmpty()) {
        ItemEnchantments enchantments = offer.getResult().get(DataComponents.STORED_ENCHANTMENTS);

        if (enchantments != null) {
          for (Holder<Enchantment> enchantmentEntry : enchantments.keySet()) {
            // Get enchantment level from the book
            int level = enchantments.getLevel(enchantmentEntry);

            // Check if this enchantment matches our filter
            if (enchantmentEntry.unwrapKey().isPresent()) {
              ResourceKey<Enchantment> enchantmentKey = enchantmentEntry.unwrapKey().get();

              for (EnchantmentWithLevel desired : desiredEnchantments) {
                if (desired.getEnchantment().equals(enchantmentKey) && desired.matchesLevel(level)) {
                  // FOUND enchantment with matching level!
                  String enchantName;
                  try {
                    enchantName = Enchantment.getFullname(enchantmentEntry, level).getString();
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

                  client.player.sendOverlayMessage(Component.literal("§a§l========================================"));
                  client.player.sendOverlayMessage(Component.literal("§a§l[DITEMUKAN!] §r§a" + enchantName));
                  client.player.sendOverlayMessage(Component.literal("§a§l========================================"));
                  client.player.sendOverlayMessage(Component.literal("§eEnchantment tersedia untuk trade!"));
                  client.player.sendOverlayMessage(Component.literal("§7Cycling dihentikan."));

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
          itemName = offer.getResult().getHoverName().getString();
        } catch (Exception e) {
          itemName = "Unknown Item";
          AutomateTradeCycling.LOGGER.warn("Failed to get item name: " + e.getMessage());
        }

        client.player.sendOverlayMessage(Component.literal("§a§l========================================"));
        client.player.sendOverlayMessage(Component.literal("§a§l[DITEMUKAN!] §r§a" + itemName));
        client.player.sendOverlayMessage(Component.literal("§a§l========================================"));
        client.player.sendOverlayMessage(Component.literal("§eItem tersedia untuk trade!"));
        client.player.sendOverlayMessage(Component.literal("§7Cycling dihentikan."));

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

  public Villager getTargetVillager() {
    return targetVillager;
  }

  // Helper untuk mendapatkan villager yang sedang dilihat player
  public static Villager getTargetedVillager(Minecraft client) {
    if (client.player == null || client.hitResult == null) {
      return null;
    }

    HitResult hitResult = client.hitResult;
    if (hitResult.getType() == HitResult.Type.ENTITY) {
      EntityHitResult entityHit = (EntityHitResult) hitResult;
      if (entityHit.getEntity() instanceof Villager) {
        return (Villager) entityHit.getEntity();
      }
    }

    return null;
  }
}

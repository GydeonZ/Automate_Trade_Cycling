package net.shuu.mod.config;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.village.VillagerProfession;
import java.util.*;

public class TradeCyclingConfig {
  private static TradeCyclingConfig INSTANCE;

  // Map untuk menyimpan item yang dicari per profesi
  private final Map<VillagerProfession, Set<Item>> desiredItems = new HashMap<>();

  // Status cycling
  private boolean isCycling = false;
  private VillagerProfession currentProfession = null;

  public static TradeCyclingConfig getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TradeCyclingConfig();
    }
    return INSTANCE;
  }

  private TradeCyclingConfig() {
    // Initialize dengan profesi default
  }

  public void setDesiredItems(VillagerProfession profession, Set<Item> items) {
    if (items == null || items.isEmpty()) {
      desiredItems.remove(profession);
    } else {
      desiredItems.put(profession, new HashSet<>(items));
    }
  }

  public Set<Item> getDesiredItems(VillagerProfession profession) {
    return desiredItems.getOrDefault(profession, Collections.emptySet());
  }

  public boolean hasDesiredItems(VillagerProfession profession) {
    return desiredItems.containsKey(profession) && !desiredItems.get(profession).isEmpty();
  }

  public void addDesiredItem(VillagerProfession profession, Item item) {
    desiredItems.computeIfAbsent(profession, k -> new HashSet<>()).add(item);
  }

  public void removeDesiredItem(VillagerProfession profession, Item item) {
    Set<Item> items = desiredItems.get(profession);
    if (items != null) {
      items.remove(item);
      if (items.isEmpty()) {
        desiredItems.remove(profession);
      }
    }
  }

  public void clearDesiredItems(VillagerProfession profession) {
    desiredItems.remove(profession);
  }

  public boolean isCycling() {
    return isCycling;
  }

  public void setCycling(boolean cycling) {
    this.isCycling = cycling;
  }

  public VillagerProfession getCurrentProfession() {
    return currentProfession;
  }

  public void setCurrentProfession(VillagerProfession profession) {
    this.currentProfession = profession;
  }

  // Method untuk mendapatkan semua item yang bisa dijual oleh profesi tertentu
  public static Map<VillagerProfession, List<Item>> getProfessionTradeables() {
    Map<VillagerProfession, List<Item>> tradeables = new HashMap<>();

    // ARMORER
    tradeables.put(VillagerProfession.ARMORER, Arrays.asList(
        Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
        Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS,
        Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS,
        Items.SHIELD, Items.BELL));

    // BUTCHER
    tradeables.put(VillagerProfession.BUTCHER, Arrays.asList(
        Items.COOKED_CHICKEN, Items.COOKED_PORKCHOP, Items.COOKED_MUTTON, Items.COOKED_RABBIT,
        Items.COOKED_BEEF, Items.RABBIT_STEW));

    // CARTOGRAPHER
    tradeables.put(VillagerProfession.CARTOGRAPHER, Arrays.asList(
        Items.MAP, Items.FILLED_MAP, Items.ITEM_FRAME, Items.GLOBE_BANNER_PATTERN,
        Items.PAINTING));

    // CLERIC
    tradeables.put(VillagerProfession.CLERIC, Arrays.asList(
        Items.REDSTONE, Items.LAPIS_LAZULI, Items.GLOWSTONE, Items.ENDER_PEARL,
        Items.EXPERIENCE_BOTTLE, Items.BREWING_STAND));

    // FARMER
    tradeables.put(VillagerProfession.FARMER, Arrays.asList(
        Items.BREAD, Items.APPLE, Items.COOKIE, Items.CAKE, Items.PUMPKIN_PIE,
        Items.SUSPICIOUS_STEW, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE));

    // FISHERMAN
    tradeables.put(VillagerProfession.FISHERMAN, Arrays.asList(
        Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON,
        Items.TROPICAL_FISH, Items.PUFFERFISH, Items.FISHING_ROD));

    // FLETCHER
    tradeables.put(VillagerProfession.FLETCHER, Arrays.asList(
        Items.ARROW, Items.TIPPED_ARROW, Items.BOW, Items.CROSSBOW,
        Items.FLINT, Items.TRIPWIRE_HOOK));

    // LEATHERWORKER
    tradeables.put(VillagerProfession.LEATHERWORKER, Arrays.asList(
        Items.LEATHER, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS,
        Items.LEATHER_HELMET, Items.LEATHER_HORSE_ARMOR, Items.SADDLE));

    // LIBRARIAN
    tradeables.put(VillagerProfession.LIBRARIAN, Arrays.asList(
        Items.BOOKSHELF, Items.ENCHANTED_BOOK, Items.CLOCK, Items.COMPASS,
        Items.GLASS, Items.NAME_TAG, Items.LANTERN));

    // MASON (STONE MASON)
    tradeables.put(VillagerProfession.MASON, Arrays.asList(
        Items.BRICK, Items.CLAY_BALL, Items.DRIPSTONE_BLOCK, Items.POLISHED_ANDESITE,
        Items.POLISHED_DIORITE, Items.POLISHED_GRANITE, Items.QUARTZ, Items.TERRACOTTA,
        Items.WHITE_GLAZED_TERRACOTTA, Items.RED_GLAZED_TERRACOTTA));

    // SHEPHERD
    tradeables.put(VillagerProfession.SHEPHERD, Arrays.asList(
        Items.WHITE_WOOL, Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL,
        Items.YELLOW_WOOL, Items.LIME_WOOL, Items.PINK_WOOL, Items.GRAY_WOOL,
        Items.LIGHT_GRAY_WOOL, Items.CYAN_WOOL, Items.PURPLE_WOOL, Items.BLUE_WOOL,
        Items.BROWN_WOOL, Items.GREEN_WOOL, Items.RED_WOOL, Items.BLACK_WOOL,
        Items.WHITE_BED, Items.PAINTING, Items.WHITE_BANNER));

    // TOOLSMITH
    tradeables.put(VillagerProfession.TOOLSMITH, Arrays.asList(
        Items.IRON_AXE, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_HOE,
        Items.DIAMOND_AXE, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE,
        Items.BELL));

    // WEAPONSMITH
    tradeables.put(VillagerProfession.WEAPONSMITH, Arrays.asList(
        Items.IRON_SWORD, Items.IRON_AXE, Items.DIAMOND_SWORD, Items.DIAMOND_AXE,
        Items.BELL, Items.ENCHANTED_BOOK));

    return tradeables;
  }
}

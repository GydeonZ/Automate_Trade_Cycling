package net.shuu.mod.config;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import java.util.*;

public class TradeCyclingConfig {
  private static TradeCyclingConfig INSTANCE;

  // Map untuk menyimpan item yang dicari per profesi
  private final Map<ResourceKey<VillagerProfession>, Set<Item>> desiredItems = new HashMap<>();

  // Map untuk menyimpan enchantment dengan level yang dicari per profesi (khusus
  // Librarian)
  private final Map<ResourceKey<VillagerProfession>, Set<EnchantmentWithLevel>> desiredEnchantments = new HashMap<>();

  // Status cycling
  private boolean isCycling = false;
  private ResourceKey<VillagerProfession> currentProfession = null;

  public static TradeCyclingConfig getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TradeCyclingConfig();
    }
    return INSTANCE;
  }

  private TradeCyclingConfig() {
    // Initialize dengan profesi default
  }

  public void setDesiredItems(ResourceKey<VillagerProfession> profession, Set<Item> items) {
    if (items == null || items.isEmpty()) {
      desiredItems.remove(profession);
    } else {
      desiredItems.put(profession, new HashSet<>(items));
    }
  }

  public Set<Item> getDesiredItems(ResourceKey<VillagerProfession> profession) {
    return desiredItems.getOrDefault(profession, Collections.emptySet());
  }

  public boolean hasDesiredItems(ResourceKey<VillagerProfession> profession) {
    return desiredItems.containsKey(profession) && !desiredItems.get(profession).isEmpty();
  }

  public void addDesiredItem(ResourceKey<VillagerProfession> profession, Item item) {
    desiredItems.computeIfAbsent(profession, k -> new HashSet<>()).add(item);
  }

  public void removeDesiredItem(ResourceKey<VillagerProfession> profession, Item item) {
    Set<Item> items = desiredItems.get(profession);
    if (items != null) {
      items.remove(item);
      if (items.isEmpty()) {
        desiredItems.remove(profession);
      }
    }
  }

  public void clearDesiredItems(ResourceKey<VillagerProfession> profession) {
    desiredItems.remove(profession);
  }

  // Enchantment methods
  public void setDesiredEnchantments(ResourceKey<VillagerProfession> profession, Set<EnchantmentWithLevel> enchantments) {
    if (enchantments == null || enchantments.isEmpty()) {
      desiredEnchantments.remove(profession);
    } else {
      desiredEnchantments.put(profession, new HashSet<>(enchantments));
    }
  }

  public Set<EnchantmentWithLevel> getDesiredEnchantments(ResourceKey<VillagerProfession> profession) {
    return desiredEnchantments.getOrDefault(profession, Collections.emptySet());
  }

  public boolean hasDesiredEnchantments(ResourceKey<VillagerProfession> profession) {
    return desiredEnchantments.containsKey(profession) && !desiredEnchantments.get(profession).isEmpty();
  }

  public void clearDesiredEnchantments(ResourceKey<VillagerProfession> profession) {
    desiredEnchantments.remove(profession);
  }

  public boolean isCycling() {
    return isCycling;
  }

  public void setCycling(boolean cycling) {
    this.isCycling = cycling;
  }

  public ResourceKey<VillagerProfession> getCurrentProfession() {
    return currentProfession;
  }

  public void setCurrentProfession(ResourceKey<VillagerProfession> profession) {
    this.currentProfession = profession;
  }

  // Method untuk mendapatkan semua item yang bisa dijual oleh profesi tertentu
  public static Map<ResourceKey<VillagerProfession>, List<Item>> getProfessionTradeables() {
    Map<ResourceKey<VillagerProfession>, List<Item>> tradeables = new HashMap<>();

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
    // Sejak 26.x, item berwarna disimpan dalam ColorCollection
    tradeables.put(VillagerProfession.MASON, Arrays.asList(
        Items.BRICK, Items.CLAY_BALL, Items.DRIPSTONE_BLOCK, Items.POLISHED_ANDESITE,
        Items.POLISHED_DIORITE, Items.POLISHED_GRANITE, Items.QUARTZ, Items.TERRACOTTA,
        Items.GLAZED_TERRACOTTA.pick(DyeColor.WHITE), Items.GLAZED_TERRACOTTA.pick(DyeColor.RED)));

    // SHEPHERD
    List<Item> shepherdTradeables = new ArrayList<>(Arrays.asList(Items.PAINTING));
    shepherdTradeables.addAll(Items.WOOL.asList()); // semua warna wool
    shepherdTradeables.add(Items.BED.pick(DyeColor.WHITE));
    shepherdTradeables.add(Items.BANNER.pick(DyeColor.WHITE));
    tradeables.put(VillagerProfession.SHEPHERD, shepherdTradeables);

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

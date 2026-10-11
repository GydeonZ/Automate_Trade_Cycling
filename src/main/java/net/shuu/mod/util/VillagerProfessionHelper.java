package net.shuu.mod.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class VillagerProfessionHelper {

  private static final Map<ResourceKey<VillagerProfession>, Block> PROFESSION_JOB_SITES = new HashMap<>();

  static {
    // Map profesi ke job site block mereka
    PROFESSION_JOB_SITES.put(VillagerProfession.ARMORER, Blocks.BLAST_FURNACE);
    PROFESSION_JOB_SITES.put(VillagerProfession.BUTCHER, Blocks.SMOKER);
    PROFESSION_JOB_SITES.put(VillagerProfession.CARTOGRAPHER, Blocks.CARTOGRAPHY_TABLE);
    PROFESSION_JOB_SITES.put(VillagerProfession.CLERIC, Blocks.BREWING_STAND);
    PROFESSION_JOB_SITES.put(VillagerProfession.FARMER, Blocks.COMPOSTER);
    PROFESSION_JOB_SITES.put(VillagerProfession.FISHERMAN, Blocks.BARREL);
    PROFESSION_JOB_SITES.put(VillagerProfession.FLETCHER, Blocks.FLETCHING_TABLE);
    PROFESSION_JOB_SITES.put(VillagerProfession.LEATHERWORKER, Blocks.CAULDRON);
    PROFESSION_JOB_SITES.put(VillagerProfession.LIBRARIAN, Blocks.LECTERN);
    PROFESSION_JOB_SITES.put(VillagerProfession.MASON, Blocks.STONECUTTER);
    PROFESSION_JOB_SITES.put(VillagerProfession.SHEPHERD, Blocks.LOOM);
    PROFESSION_JOB_SITES.put(VillagerProfession.TOOLSMITH, Blocks.SMITHING_TABLE);
    PROFESSION_JOB_SITES.put(VillagerProfession.WEAPONSMITH, Blocks.GRINDSTONE);
  }

  /**
   * Mendapatkan job site block untuk profesi tertentu
   */
  public static Block getJobSiteForProfession(ResourceKey<VillagerProfession> profession) {
    return PROFESSION_JOB_SITES.get(profession);
  }

  /**
   * Mendapatkan profesi dari job site block
   */
  public static ResourceKey<VillagerProfession> getProfessionFromJobSite(Block block) {
    for (Map.Entry<ResourceKey<VillagerProfession>, Block> entry : PROFESSION_JOB_SITES.entrySet()) {
      if (entry.getValue() == block) {
        return entry.getKey();
      }
    }
    return VillagerProfession.NONE;
  }

  /**
   * Cek apakah block adalah job site block
   */
  public static boolean isJobSiteBlock(Block block) {
    return PROFESSION_JOB_SITES.containsValue(block);
  }

  /**
   * Mendapatkan nama display profesi
   */
  public static String getProfessionDisplayName(ResourceKey<VillagerProfession> profession) {
    if (VillagerProfession.ARMORER.equals(profession))
      return "Armorer";
    if (VillagerProfession.BUTCHER.equals(profession))
      return "Butcher";
    if (VillagerProfession.CARTOGRAPHER.equals(profession))
      return "Cartographer";
    if (VillagerProfession.CLERIC.equals(profession))
      return "Cleric";
    if (VillagerProfession.FARMER.equals(profession))
      return "Farmer";
    if (VillagerProfession.FISHERMAN.equals(profession))
      return "Fisherman";
    if (VillagerProfession.FLETCHER.equals(profession))
      return "Fletcher";
    if (VillagerProfession.LEATHERWORKER.equals(profession))
      return "Leatherworker";
    if (VillagerProfession.LIBRARIAN.equals(profession))
      return "Librarian";
    if (VillagerProfession.MASON.equals(profession))
      return "Mason";
    if (VillagerProfession.SHEPHERD.equals(profession))
      return "Shepherd";
    if (VillagerProfession.TOOLSMITH.equals(profession))
      return "Toolsmith";
    if (VillagerProfession.WEAPONSMITH.equals(profession))
      return "Weaponsmith";
    if (VillagerProfession.NITWIT.equals(profession))
      return "Nitwit";
    return "Unemployed";
  }
}

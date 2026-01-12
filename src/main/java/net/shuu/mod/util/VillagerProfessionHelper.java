package net.shuu.mod.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.village.VillagerProfession;

import java.util.HashMap;
import java.util.Map;

public class VillagerProfessionHelper {

  private static final Map<VillagerProfession, Block> PROFESSION_JOB_SITES = new HashMap<>();

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
  public static Block getJobSiteForProfession(VillagerProfession profession) {
    return PROFESSION_JOB_SITES.get(profession);
  }

  /**
   * Mendapatkan profesi dari job site block
   */
  public static VillagerProfession getProfessionFromJobSite(Block block) {
    for (Map.Entry<VillagerProfession, Block> entry : PROFESSION_JOB_SITES.entrySet()) {
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
  public static String getProfessionDisplayName(VillagerProfession profession) {
    if (profession == VillagerProfession.ARMORER)
      return "Armorer";
    if (profession == VillagerProfession.BUTCHER)
      return "Butcher";
    if (profession == VillagerProfession.CARTOGRAPHER)
      return "Cartographer";
    if (profession == VillagerProfession.CLERIC)
      return "Cleric";
    if (profession == VillagerProfession.FARMER)
      return "Farmer";
    if (profession == VillagerProfession.FISHERMAN)
      return "Fisherman";
    if (profession == VillagerProfession.FLETCHER)
      return "Fletcher";
    if (profession == VillagerProfession.LEATHERWORKER)
      return "Leatherworker";
    if (profession == VillagerProfession.LIBRARIAN)
      return "Librarian";
    if (profession == VillagerProfession.MASON)
      return "Mason";
    if (profession == VillagerProfession.SHEPHERD)
      return "Shepherd";
    if (profession == VillagerProfession.TOOLSMITH)
      return "Toolsmith";
    if (profession == VillagerProfession.WEAPONSMITH)
      return "Weaponsmith";
    if (profession == VillagerProfession.NITWIT)
      return "Nitwit";
    return "Unemployed";
  }
}

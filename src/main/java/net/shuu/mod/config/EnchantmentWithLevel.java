package net.shuu.mod.config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;

import java.util.Objects;

/**
 * Class untuk menyimpan enchantment beserta level yang diinginkan
 */
public class EnchantmentWithLevel {
  private final RegistryKey<Enchantment> enchantment;
  private final int minLevel;
  private final int maxLevel;

  public EnchantmentWithLevel(RegistryKey<Enchantment> enchantment, int minLevel, int maxLevel) {
    this.enchantment = enchantment;
    this.minLevel = minLevel;
    this.maxLevel = maxLevel;
  }

  public EnchantmentWithLevel(RegistryKey<Enchantment> enchantment, int level) {
    this(enchantment, level, level);
  }

  public RegistryKey<Enchantment> getEnchantment() {
    return enchantment;
  }

  public int getMinLevel() {
    return minLevel;
  }

  public int getMaxLevel() {
    return maxLevel;
  }

  /**
   * Check if the given level matches this filter
   */
  public boolean matchesLevel(int level) {
    return level >= minLevel && level <= maxLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    EnchantmentWithLevel that = (EnchantmentWithLevel) o;
    return minLevel == that.minLevel && maxLevel == that.maxLevel &&
        Objects.equals(enchantment, that.enchantment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enchantment, minLevel, maxLevel);
  }

  @Override
  public String toString() {
    if (minLevel == maxLevel) {
      return enchantment.getValue().toString() + " " + minLevel;
    }
    return enchantment.getValue().toString() + " " + minLevel + "-" + maxLevel;
  }
}

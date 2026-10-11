package net.shuu.mod.mixin;

import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor untuk mendapatkan merchant dari MerchantMenu
 * Diperlukan untuk server-side cycling logic
 */
@Mixin(MerchantMenu.class)
public interface MerchantScreenHandlerAccessor {

  @Accessor("trader")
  Merchant getMerchant();
}

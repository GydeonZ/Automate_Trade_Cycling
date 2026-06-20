package net.shuu.mod.mixin;

import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.village.Merchant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor untuk mendapatkan merchant dari MerchantScreenHandler
 * Diperlukan untuk server-side cycling logic
 */
@Mixin(MerchantScreenHandler.class)
public interface MerchantScreenHandlerAccessor {

  @Accessor("merchant")
  Merchant getMerchant();
}

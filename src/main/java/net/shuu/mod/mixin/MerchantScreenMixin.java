package net.shuu.mod.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.village.TradeOfferList;
import net.shuu.mod.manager.TradeCyclingManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantScreen.class)
public class MerchantScreenMixin {

  private int checkDelay = 0;
  private static final int CHECK_INTERVAL = 10; // Check setiap 10 ticks (0.5 detik)

  /**
   * Hook ke render method untuk check trade offers setiap frame
   */
  @Inject(method = "render", at = @At("HEAD"))
  private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
    TradeCyclingManager manager = TradeCyclingManager.getInstance();

    // Hanya check jika sedang cycling
    if (!manager.isSearching()) {
      return;
    }

    // Delay untuk tidak spam check
    if (checkDelay > 0) {
      checkDelay--;
      return;
    }

    MerchantScreen screen = (MerchantScreen) (Object) this;

    try {
      TradeOfferList offers = screen.getScreenHandler().getRecipes();

      if (offers != null && !offers.isEmpty()) {
        net.shuu.mod.AutomateTradeCycling.LOGGER
            .info("[MIXIN] Checking offers from render, offers count: " + offers.size());
        boolean found = manager.checkCurrentOffers(offers);

        if (found) {
          net.shuu.mod.AutomateTradeCycling.LOGGER.info("[MIXIN] Found desired item! Stopping checks.");
        }
      }
    } catch (Exception e) {
      net.shuu.mod.AutomateTradeCycling.LOGGER.warn("[MIXIN] Error checking offers: " + e.getMessage());
    }

    checkDelay = CHECK_INTERVAL;
  }
}

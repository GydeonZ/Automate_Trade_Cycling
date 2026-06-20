package net.shuu.mod.mixin;

import net.minecraft.entity.passive.VillagerEntity;
import net.shuu.mod.manager.TradeCyclingManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {

  /**
   * Hook ke method tick untuk detect perubahan trade offers
   * Ini akan dipanggil setiap tick saat villager di-update
   */
  @Inject(method = "tick", at = @At("TAIL"))
  private void onVillagerTick(CallbackInfo ci) {
    VillagerEntity villager = (VillagerEntity) (Object) this;
    TradeCyclingManager manager = TradeCyclingManager.getInstance();

    // Cek apakah villager ini sedang di-cycle
    if (manager.isSearching() && villager == manager.getTargetVillager()) {
      // Manager akan handle pengecekan trade offers di tick method-nya sendiri
      // Mixin ini hanya untuk memastikan kita bisa track state villager
    }
  }
}

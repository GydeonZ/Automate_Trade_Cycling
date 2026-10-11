package net.shuu.mod.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Invokes vanilla's trade generation after the current offers are cleared. */
@Mixin(Villager.class)
public interface VillagerTradeAccessor {
  @Invoker("updateTrades")
  void invokeUpdateTrades(ServerLevel level);
}

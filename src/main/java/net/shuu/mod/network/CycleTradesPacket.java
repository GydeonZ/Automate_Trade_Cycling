package net.shuu.mod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.shuu.mod.AutomateTradeCycling;

public record CycleTradesPacket() implements CustomPacketPayload {
  public static final Type<CycleTradesPacket> ID = new Type<>(
      Identifier.fromNamespaceAndPath(AutomateTradeCycling.MOD_ID, "cycle_trades"));
  public static final StreamCodec<RegistryFriendlyByteBuf, CycleTradesPacket> CODEC =
      StreamCodec.unit(new CycleTradesPacket());

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return ID;
  }
}

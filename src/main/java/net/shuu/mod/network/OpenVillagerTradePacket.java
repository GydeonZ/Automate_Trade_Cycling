package net.shuu.mod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.shuu.mod.AutomateTradeCycling;

public record OpenVillagerTradePacket(int entityId) implements CustomPacketPayload {
  public static final Type<OpenVillagerTradePacket> ID = new Type<>(
      Identifier.fromNamespaceAndPath(AutomateTradeCycling.MOD_ID, "open_villager_trade"));
  public static final StreamCodec<RegistryFriendlyByteBuf, OpenVillagerTradePacket> CODEC =
      StreamCodec.composite(ByteBufCodecs.VAR_INT, OpenVillagerTradePacket::entityId,
          OpenVillagerTradePacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return ID;
  }
}

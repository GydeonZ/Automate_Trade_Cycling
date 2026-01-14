package net.shuu.mod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.shuu.mod.AutomateTradeCycling;

/**
 * Packet untuk mengirim request cycling dari client ke server
 * Mirip dengan CycleTradesPacket dari trade-cycling mod
 * Menggunakan sistem CustomPayload dari Fabric API 1.21+
 */
public record CycleTradesPacket() implements CustomPayload {

  public static final CustomPayload.Id<CycleTradesPacket> ID = new CustomPayload.Id<>(
      Identifier.of(AutomateTradeCycling.MOD_ID, "cycle_trades"));

  public static final PacketCodec<RegistryByteBuf, CycleTradesPacket> CODEC = PacketCodec.unit(new CycleTradesPacket());

  @Override
  public Id<? extends CustomPayload> getId() {
    return ID;
  }
}

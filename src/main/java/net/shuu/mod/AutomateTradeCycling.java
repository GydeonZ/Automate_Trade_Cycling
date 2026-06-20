package net.shuu.mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.shuu.mod.network.CycleTradesPacket;
import net.shuu.mod.network.PacketHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutomateTradeCycling implements ModInitializer {
	public static final String MOD_ID = "automate-trade-cycling";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Register networking
		registerPackets();

		LOGGER.info("Automate Trade Cycling initialized!");
	}

	private void registerPackets() {
		// Register payload type
		PayloadTypeRegistry.playC2S().register(CycleTradesPacket.ID, CycleTradesPacket.CODEC);

		// Register server-side packet handler
		ServerPlayNetworking.registerGlobalReceiver(CycleTradesPacket.ID, (payload, context) -> {
			context.server().execute(() -> {
				PacketHandler.onCycleTrades(context.player());
			});
		});

		LOGGER.info("Registered CycleTradesPacket handler");
	}
}
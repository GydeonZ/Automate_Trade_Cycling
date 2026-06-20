package net.shuu.mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.shuu.mod.keybind.ModKeyBindings;
import net.shuu.mod.manager.TradeCyclingManager;

public class AutomateTradeCyclingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register keybindings
        ModKeyBindings.register();

        // Register client tick untuk trade cycling manager
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            TradeCyclingManager.getInstance().tick(client);
        });

        AutomateTradeCycling.LOGGER.info("Automate Trade Cycling Client initialized!");
    }
}

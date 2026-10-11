package net.shuu.mod.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.resources.ResourceKey;
import net.shuu.mod.manager.TradeCyclingManager;
import net.shuu.mod.screen.EnchantmentSelectionScreen;
import net.shuu.mod.screen.ItemSelectionScreen;
import net.shuu.mod.AutomateTradeCycling;

public class ModKeyBindings {

  private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(AutomateTradeCycling.MOD_ID, "main"));

  public static KeyMapping openItemSelectionKey;
  public static KeyMapping startStopCyclingKey;

  public static void register() {
    // Keybind untuk membuka GUI pemilihan item (default: V)
    openItemSelectionKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        "key.automate-trade-cycling.open_selection",
        InputConstants.Type.KEYBOARD,
        86,
        CATEGORY));

    // Keybind untuk start/stop cycling (default: C)
    startStopCyclingKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        "key.automate-trade-cycling.toggle_cycling",
        InputConstants.Type.KEYBOARD,
        67,
        CATEGORY));

    // Register tick event untuk handle key press
    ClientTickEvents.END_CLIENT_TICK.register(client -> {
      handleKeyPress(client);
    });
  }

  private static void handleKeyPress(Minecraft client) {
    // Handle open item selection GUI
    if (openItemSelectionKey.consumeClick()) {
      Villager villager = TradeCyclingManager.getTargetedVillager(client);

      if (villager != null) {
        ResourceKey<VillagerProfession> profession = villager.getVillagerData().profession().unwrapKey().orElse(VillagerProfession.NONE);

        if (profession == VillagerProfession.NONE || profession == VillagerProfession.NITWIT) {
          if (client.player != null) {
            client.player.sendOverlayMessage(Component.literal("§cVillager ini tidak memiliki profesi!"));
          }
          return;
        }

        // Untuk Librarian, buka enchantment selection screen
        if (profession == VillagerProfession.LIBRARIAN) {
          client.setScreenAndShow(new EnchantmentSelectionScreen(villager));
        } else {
          // Untuk profesi lain, buka item selection screen
          client.setScreenAndShow(new ItemSelectionScreen(villager));
        }
      } else {
        if (client.player != null) {
          client.player.sendOverlayMessage(Component.literal("§cArahkan crosshair ke villager terlebih dahulu!"));
        }
      }
    }

    // Handle toggle cycling
    if (startStopCyclingKey.consumeClick()) {
      TradeCyclingManager manager = TradeCyclingManager.getInstance();

      if (manager.isSearching()) {
        // Stop cycling
        manager.stopCycling();
        if (client.player != null) {
          client.player.sendOverlayMessage(Component.literal("§eTrade cycling dihentikan!"));
        }
      } else {
        // Start cycling dengan villager yang sedang ditarget
        Villager villager = TradeCyclingManager.getTargetedVillager(client);

        if (villager != null) {
          if (villager.getVillagerData().profession().unwrapKey().orElse(VillagerProfession.NONE) == net.minecraft.world.entity.npc.villager.VillagerProfession.NONE ||
              villager.getVillagerData().profession().unwrapKey().orElse(VillagerProfession.NONE) == net.minecraft.world.entity.npc.villager.VillagerProfession.NITWIT) {
            if (client.player != null) {
              client.player.sendOverlayMessage(Component.literal("§cVillager ini tidak memiliki profesi!"));
            }
            return;
          }

          manager.startCycling(villager);
        } else {
          if (client.player != null) {
            client.player.sendOverlayMessage(Component.literal("§cArahkan crosshair ke villager terlebih dahulu!"));
          }
        }
      }
    }
  }
}

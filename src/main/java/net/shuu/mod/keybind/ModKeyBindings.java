package net.shuu.mod.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.shuu.mod.AutomateTradeCycling;
import net.shuu.mod.manager.TradeCyclingManager;
import net.shuu.mod.screen.EnchantmentSelectionScreen;
import net.shuu.mod.screen.ItemSelectionScreen;

public class ModKeyBindings {

  public static final KeyMapping.Category CATEGORY = KeyMapping.Category
      .register(Identifier.fromNamespaceAndPath(AutomateTradeCycling.MOD_ID, "main"));

  public static KeyMapping openItemSelectionKey;
  public static KeyMapping startStopCyclingKey;

  public static void register() {
    // Keybind untuk membuka GUI pemilihan item (default: V)
    openItemSelectionKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        "key.automate-trade-cycling.open_selection",
        InputConstants.Type.KEYBOARD,
        InputConstants.KEY_V,
        CATEGORY));

    // Keybind untuk start/stop cycling (default: C)
    startStopCyclingKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        "key.automate-trade-cycling.toggle_cycling",
        InputConstants.Type.KEYBOARD,
        InputConstants.KEY_C,
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
        Holder<VillagerProfession> profession = villager.getVillagerData().profession();

        if (profession.is(VillagerProfession.NONE) || profession.is(VillagerProfession.NITWIT)) {
          if (client.player != null) {
            client.player.sendSystemMessage(Component.literal("§cVillager ini tidak memiliki profesi!"));
          }
          return;
        }

        // Untuk Librarian, buka enchantment selection screen
        if (profession.is(VillagerProfession.LIBRARIAN)) {
          client.setScreenAndShow(new EnchantmentSelectionScreen(villager));
        } else {
          // Untuk profesi lain, buka item selection screen
          client.setScreenAndShow(new ItemSelectionScreen(villager));
        }
      } else {
        if (client.player != null) {
          client.player.sendSystemMessage(Component.literal("§cArahkan crosshair ke villager terlebih dahulu!"));
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
          client.player.sendSystemMessage(Component.literal("§eTrade cycling dihentikan!"));
        }
      } else {
        // Start cycling dengan villager yang sedang ditarget
        Villager villager = TradeCyclingManager.getTargetedVillager(client);

        if (villager != null) {
          Holder<VillagerProfession> profession = villager.getVillagerData().profession();

          if (profession.is(VillagerProfession.NONE) || profession.is(VillagerProfession.NITWIT)) {
            if (client.player != null) {
              client.player.sendSystemMessage(Component.literal("§cVillager ini tidak memiliki profesi!"));
            }
            return;
          }

          manager.startCycling(villager);
        } else {
          if (client.player != null) {
            client.player.sendSystemMessage(Component.literal("§cArahkan crosshair ke villager terlebih dahulu!"));
          }
        }
      }
    }
  }
}

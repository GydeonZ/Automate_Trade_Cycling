package net.shuu.mod.keybind;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.shuu.mod.manager.TradeCyclingManager;
import net.shuu.mod.screen.ItemSelectionScreen;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {

  public static KeyBinding openItemSelectionKey;
  public static KeyBinding startStopCyclingKey;

  public static void register() {
    // Keybind untuk membuka GUI pemilihan item (default: V)
    openItemSelectionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.automate-trade-cycling.open_selection",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_V,
        "category.automate-trade-cycling"));

    // Keybind untuk start/stop cycling (default: C)
    startStopCyclingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.automate-trade-cycling.toggle_cycling",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_C,
        "category.automate-trade-cycling"));

    // Register tick event untuk handle key press
    ClientTickEvents.END_CLIENT_TICK.register(client -> {
      handleKeyPress(client);
    });
  }

  private static void handleKeyPress(MinecraftClient client) {
    // Handle open item selection GUI
    if (openItemSelectionKey.wasPressed()) {
      VillagerEntity villager = TradeCyclingManager.getTargetedVillager(client);

      if (villager != null) {
        if (villager.getVillagerData().getProfession() == net.minecraft.village.VillagerProfession.NONE ||
            villager.getVillagerData().getProfession() == net.minecraft.village.VillagerProfession.NITWIT) {
          if (client.player != null) {
            client.player.sendMessage(Text.literal("§cVillager ini tidak memiliki profesi!"), false);
          }
          return;
        }

        client.setScreen(new ItemSelectionScreen(villager));
      } else {
        if (client.player != null) {
          client.player.sendMessage(Text.literal("§cArahkan crosshair ke villager terlebih dahulu!"), false);
        }
      }
    }

    // Handle toggle cycling
    if (startStopCyclingKey.wasPressed()) {
      TradeCyclingManager manager = TradeCyclingManager.getInstance();

      if (manager.isSearching()) {
        // Stop cycling
        manager.stopCycling();
        if (client.player != null) {
          client.player.sendMessage(Text.literal("§eTrade cycling dihentikan!"), false);
        }
      } else {
        // Start cycling dengan villager yang sedang ditarget
        VillagerEntity villager = TradeCyclingManager.getTargetedVillager(client);

        if (villager != null) {
          if (villager.getVillagerData().getProfession() == net.minecraft.village.VillagerProfession.NONE ||
              villager.getVillagerData().getProfession() == net.minecraft.village.VillagerProfession.NITWIT) {
            if (client.player != null) {
              client.player.sendMessage(Text.literal("§cVillager ini tidak memiliki profesi!"), false);
            }
            return;
          }

          manager.startCycling(villager);
        } else {
          if (client.player != null) {
            client.player.sendMessage(Text.literal("§cArahkan crosshair ke villager terlebih dahulu!"), false);
          }
        }
      }
    }
  }
}

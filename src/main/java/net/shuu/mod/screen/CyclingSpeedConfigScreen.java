package net.shuu.mod.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.CharacterEvent;
import net.shuu.mod.manager.TradeCyclingManager;

/**
 * Screen untuk konfigurasi kecepatan cycling dalam milliseconds
 */
public class CyclingSpeedConfigScreen extends Screen {
  private final Screen parent;
  private EditBox speedField;

  // Store current value
  private int speedMs;

  public CyclingSpeedConfigScreen(Screen parent) {
    super(Component.literal("Pengaturan Kecepatan Cycling"));
    this.parent = parent;

    // Get current speed value from manager (convert ticks to ms)
    this.speedMs = TradeCyclingManager.getInstance().getCyclingDelayMs();
  }

  @Override
  protected void init() {
    super.init();

    int centerX = this.width / 2;
    int startY = 80;
    int fieldWidth = 100;

    // Speed field
    speedField = new EditBox(this.font, centerX - fieldWidth / 2, startY, fieldWidth, 20,
        Component.literal("Jeda (ms)"));
    speedField.setValue(String.valueOf(speedMs));
    speedField.setMaxLength(5);
    speedField.setFocused(true);
    this.addRenderableWidget(speedField);
    this.setInitialFocus(speedField);

    // Save button
    this.addRenderableWidget(Button.builder(Component.literal("Simpan"), button -> {
      if (applyChanges()) {
        this.onClose();
      }
    })
        .bounds(centerX - 105, this.height - 40, 100, 20)
        .build());

    // Cancel button
    this.addRenderableWidget(Button.builder(Component.literal("Batal"), button -> {
      this.onClose();
    })
        .bounds(centerX + 5, this.height - 40, 100, 20)
        .build());

    // Reset to default button
    this.addRenderableWidget(Button.builder(Component.literal("Default"), button -> {
      speedField.setValue("1000");
    })
        .bounds(centerX - 60, this.height - 70, 120, 20)
        .build());
  }

  private boolean applyChanges() {
    try {
      int speed = Integer.parseInt(speedField.getValue());

      // Validate range (50ms to 10000ms)
      if (speed < 50 || speed > 10000) {
        if (this.minecraft != null && this.minecraft.player != null) {
          this.minecraft.player.sendOverlayMessage(Component.literal("Jeda harus antara 50 dan 10000 ms."));
        }
        return false;
      }

      // Apply changes
      TradeCyclingManager.getInstance().setCyclingDelayMs(speed);

      if (this.minecraft != null && this.minecraft.player != null) {
        this.minecraft.player
            .sendOverlayMessage(Component.literal("Kecepatan cycling disimpan: " + speed + " ms."));
      }
      return true;
    } catch (NumberFormatException e) {
      if (this.minecraft != null && this.minecraft.player != null) {
        this.minecraft.player.sendOverlayMessage(Component.literal("Masukkan angka jeda yang valid."));
      }
      return false;
    }
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
    super.extractRenderState(context, mouseX, mouseY, delta);

    // Title
    context.centeredText(this.font, this.title,
        this.width / 2, 30, 0xFFFFFF);

    // Label
    int centerX = this.width / 2;
    int startY = 80;

    context.centeredText(this.font,
        Component.literal("Jeda antar cycling (milidetik):"),
        centerX, startY - 20, 0xFFFFFF);

    // Render text field on top


    // Instructions and practical effect
    context.centeredText(this.font,
        Component.literal("1000 ms = 1 detik; rentang yang tersedia 50–10000 ms."),
        this.width / 2, startY + 40, 0xFFFFFF);

    context.centeredText(this.font,
        Component.literal("Jeda lebih kecil = trade lebih sering diacak."),
        this.width / 2, startY + 55, 0xFFFFFF);

    context.centeredText(this.font,
        Component.literal("Kecepatan tinggi menambah beban proses dan packet pada server."),
        this.width / 2, startY + 70, 0xFFFFFF);

    context.centeredText(this.font,
        Component.literal("Jika terasa lag atau trade tidak stabil, naikkan jedanya."),
        this.width / 2, startY + 85, 0xFFFFFF);

    context.centeredText(this.font,
        Component.literal("Interval mengikuti tick Minecraft (sekitar 50 ms per tick)."),
        this.width / 2, startY + 100, 0xAAAAAA);
  }

  @Override
  public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
    if (speedField.mouseClicked(event, doubleClick)) {
      return true;
    }
    return super.mouseClicked(event, doubleClick);
  }

  @Override
  public boolean keyPressed(KeyEvent event) {
    if (speedField.keyPressed(event)) {
      return true;
    }
    return super.keyPressed(event);
  }

  @Override
  public boolean charTyped(CharacterEvent event) {
    // Only allow digits
    if (Character.isDigit(event.codepoint())) {
      if (speedField.charTyped(event)) {
        return true;
      }
    }
    return super.charTyped(event);
  }

  @Override
  public void onClose() {
    if (this.minecraft != null) {
      this.minecraft.setScreenAndShow(parent);
    }
  }

  @Override
  public boolean isPauseScreen() {
    return true;
  }
}

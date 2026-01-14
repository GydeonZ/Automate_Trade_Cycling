package net.shuu.mod.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.shuu.mod.manager.TradeCyclingManager;

/**
 * Screen untuk konfigurasi kecepatan cycling dalam milliseconds
 */
public class CyclingSpeedConfigScreen extends Screen {
  private final Screen parent;
  private TextFieldWidget speedField;

  // Store current value
  private int speedMs;

  public CyclingSpeedConfigScreen(Screen parent) {
    super(Text.literal("Cycling Speed Configuration"));
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
    speedField = new TextFieldWidget(this.textRenderer, centerX - fieldWidth / 2, startY, fieldWidth, 20,
        Text.literal("Speed"));
    speedField.setText(String.valueOf(speedMs));
    speedField.setMaxLength(5);
    speedField.setFocusUnlocked(true);
    this.addSelectableChild(speedField);
    this.setInitialFocus(speedField);

    // Save button
    this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), button -> {
      if (applyChanges()) {
        this.close();
      }
    })
        .dimensions(centerX - 105, this.height - 40, 100, 20)
        .build());

    // Cancel button
    this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> {
      this.close();
    })
        .dimensions(centerX + 5, this.height - 40, 100, 20)
        .build());

    // Reset to default button
    this.addDrawableChild(ButtonWidget.builder(Text.literal("Reset Default"), button -> {
      speedField.setText("1000");
    })
        .dimensions(centerX - 60, this.height - 70, 120, 20)
        .build());
  }

  private boolean applyChanges() {
    try {
      int speed = Integer.parseInt(speedField.getText());

      // Validate range (50ms to 10000ms)
      if (speed < 50 || speed > 10000) {
        if (this.client != null && this.client.player != null) {
          this.client.player.sendMessage(Text.literal("§cSpeed must be between 50ms and 10000ms!"), false);
        }
        return false;
      }

      // Apply changes
      TradeCyclingManager.getInstance().setCyclingDelayMs(speed);

      if (this.client != null && this.client.player != null) {
        this.client.player
            .sendMessage(Text.literal("§aCycling speed updated to " + speed + "ms (" + (speed / 1000.0) + "s)"), false);
      }
      return true;
    } catch (NumberFormatException e) {
      if (this.client != null && this.client.player != null) {
        this.client.player.sendMessage(Text.literal("§cInvalid number format!"), false);
      }
      return false;
    }
  }

  @Override
  public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    this.renderBackground(context, mouseX, mouseY, delta);
    super.render(context, mouseX, mouseY, delta);

    // Title
    context.drawCenteredTextWithShadow(this.textRenderer, this.title,
        this.width / 2, 30, 0xFFFFFF);

    // Label
    int centerX = this.width / 2;
    int startY = 80;

    context.drawCenteredTextWithShadow(this.textRenderer,
        Text.literal("§e⏱ Cycling Speed (milliseconds):"),
        centerX, startY - 20, 0xFFFFFF);

    // Render text field on top
    speedField.render(context, mouseX, mouseY, delta);

    // Instructions
    context.drawCenteredTextWithShadow(this.textRenderer,
        Text.literal("§71000ms = 1 second"),
        this.width / 2, startY + 40, 0xFFFFFF);

    context.drawCenteredTextWithShadow(this.textRenderer,
        Text.literal("§7Lower value = faster cycling"),
        this.width / 2, startY + 55, 0xFFFFFF);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (speedField.mouseClicked(mouseX, mouseY, button)) {
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (speedField.keyPressed(keyCode, scanCode, modifiers)) {
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char chr, int modifiers) {
    // Only allow digits
    if (Character.isDigit(chr)) {
      if (speedField.charTyped(chr, modifiers)) {
        return true;
      }
    }
    return super.charTyped(chr, modifiers);
  }

  @Override
  public void close() {
    if (this.client != null) {
      this.client.setScreen(parent);
    }
  }

  @Override
  public boolean shouldPause() {
    return true;
  }
}

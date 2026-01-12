package net.shuu.mod.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.village.VillagerProfession;
import net.shuu.mod.config.TradeCyclingConfig;
import net.shuu.mod.manager.TradeCyclingManager;
import net.shuu.mod.util.VillagerProfessionHelper;

import java.util.*;

public class ItemSelectionScreen extends Screen {
  private final VillagerEntity villager;
  private final VillagerProfession profession;
  private final List<Item> availableItems;
  private final Set<Item> selectedItems;

  private static final int ITEMS_PER_ROW = 9;
  private static final int ITEM_SIZE = 20;
  private static final int ITEM_SPACING = 24;

  private int scrollOffset = 0;
  private final int maxScroll;

  public ItemSelectionScreen(VillagerEntity villager) {
    super(Text.literal("Pilih Item untuk Trade Cycling"));
    this.villager = villager;
    this.profession = villager.getVillagerData().getProfession();

    // Ambil daftar item yang bisa dijual profesi ini
    Map<VillagerProfession, List<Item>> tradeables = TradeCyclingConfig.getProfessionTradeables();
    this.availableItems = tradeables.getOrDefault(profession, new ArrayList<>());

    // Load item yang sudah dipilih sebelumnya
    this.selectedItems = new HashSet<>(TradeCyclingConfig.getInstance().getDesiredItems(profession));

    // Hitung max scroll
    int rows = (int) Math.ceil((double) availableItems.size() / ITEMS_PER_ROW);
    this.maxScroll = Math.max(0, rows - 4);
  }

  @Override
  protected void init() {
    super.init();

    int buttonY = this.height - 40;
    int buttonWidth = 100;
    int buttonSpacing = 10;

    // Tombol Start Cycling
    this.addDrawableChild(ButtonWidget.builder(Text.literal("Start Cycling"), button -> {
      if (!selectedItems.isEmpty()) {
        TradeCyclingConfig.getInstance().setDesiredItems(profession, selectedItems);
        TradeCyclingManager.getInstance().startCycling(villager);
        this.close();
      }
    })
        .dimensions(this.width / 2 - buttonWidth - buttonSpacing / 2, buttonY, buttonWidth, 20)
        .build());

    // Tombol Cancel
    this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> {
      this.close();
    })
        .dimensions(this.width / 2 + buttonSpacing / 2, buttonY, buttonWidth, 20)
        .build());

    // Tombol Clear All
    this.addDrawableChild(ButtonWidget.builder(Text.literal("Clear All"), button -> {
      selectedItems.clear();
    })
        .dimensions(10, this.height - 40, 80, 20)
        .build());

    // Tombol Select All
    this.addDrawableChild(ButtonWidget.builder(Text.literal("Select All"), button -> {
      selectedItems.clear();
      selectedItems.addAll(availableItems);
    })
        .dimensions(100, this.height - 40, 80, 20)
        .build());
  }

  @Override
  public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    // Background
    this.renderBackground(context, mouseX, mouseY, delta);

    // Title
    context.drawCenteredTextWithShadow(this.textRenderer, this.title,
        this.width / 2, 15, 0xFFFFFF);

    // Profession name
    String professionName = VillagerProfessionHelper.getProfessionDisplayName(profession);
    context.drawCenteredTextWithShadow(this.textRenderer,
        Text.literal("Profesi: " + professionName),
        this.width / 2, 30, 0xFFFF00);

    // Selected items count
    context.drawTextWithShadow(this.textRenderer,
        Text.literal("§aDipilih: " + selectedItems.size() + "/" + availableItems.size()),
        10, 10, 0xFFFFFF);

    // Item grid
    renderItemGrid(context, mouseX, mouseY);

    // Instructions
    context.drawCenteredTextWithShadow(this.textRenderer,
        Text.literal("§7Klik item untuk memilih/membatalkan pilihan"),
        this.width / 2, this.height - 60, 0xFFFFFF);

    super.render(context, mouseX, mouseY, delta);
  }

  private void renderItemGrid(DrawContext context, int mouseX, int mouseY) {
    int startX = this.width / 2 - (ITEMS_PER_ROW * ITEM_SPACING) / 2;
    int startY = 55;

    int index = 0;
    for (int row = scrollOffset; row < Math.min(scrollOffset + 4,
        (int) Math.ceil((double) availableItems.size() / ITEMS_PER_ROW)); row++) {
      for (int col = 0; col < ITEMS_PER_ROW; col++) {
        int itemIndex = row * ITEMS_PER_ROW + col;
        if (itemIndex >= availableItems.size())
          break;

        Item item = availableItems.get(itemIndex);
        int x = startX + col * ITEM_SPACING;
        int y = startY + (row - scrollOffset) * ITEM_SPACING;

        // Draw slot background
        boolean isSelected = selectedItems.contains(item);
        int color = isSelected ? 0xFF00FF00 : 0xFF8B8B8B;
        context.fill(x - 1, y - 1, x + ITEM_SIZE + 1, y + ITEM_SIZE + 1, color);
        context.fill(x, y, x + ITEM_SIZE, y + ITEM_SIZE, 0xFF373737);

        // Draw item
        context.drawItem(new ItemStack(item), x + 2, y + 2);

        // Highlight on hover
        if (mouseX >= x && mouseX < x + ITEM_SIZE && mouseY >= y && mouseY < y + ITEM_SIZE) {
          context.fill(x, y, x + ITEM_SIZE, y + ITEM_SIZE, 0x80FFFFFF);

          // Draw tooltip
          context.drawTooltip(this.textRenderer, item.getName(), mouseX, mouseY);
        }
      }
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (button == 0) { // Left click
      int startX = this.width / 2 - (ITEMS_PER_ROW * ITEM_SPACING) / 2;
      int startY = 55;

      for (int row = scrollOffset; row < Math.min(scrollOffset + 4,
          (int) Math.ceil((double) availableItems.size() / ITEMS_PER_ROW)); row++) {
        for (int col = 0; col < ITEMS_PER_ROW; col++) {
          int itemIndex = row * ITEMS_PER_ROW + col;
          if (itemIndex >= availableItems.size())
            break;

          int x = startX + col * ITEM_SPACING;
          int y = startY + (row - scrollOffset) * ITEM_SPACING;

          if (mouseX >= x && mouseX < x + ITEM_SIZE && mouseY >= y && mouseY < y + ITEM_SIZE) {
            Item item = availableItems.get(itemIndex);
            if (selectedItems.contains(item)) {
              selectedItems.remove(item);
            } else {
              selectedItems.add(item);
            }
            return true;
          }
        }
      }
    }

    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    if (verticalAmount > 0) {
      scrollOffset = Math.max(0, scrollOffset - 1);
    } else if (verticalAmount < 0) {
      scrollOffset = Math.min(maxScroll, scrollOffset + 1);
    }
    return true;
  }

  @Override
  public boolean shouldPause() {
    return false;
  }
}

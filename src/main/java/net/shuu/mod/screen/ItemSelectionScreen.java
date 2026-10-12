package net.shuu.mod.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.client.input.MouseButtonEvent;
import net.shuu.mod.config.TradeCyclingConfig;
import net.shuu.mod.manager.TradeCyclingManager;
import net.shuu.mod.screen.CyclingSpeedConfigScreen;
import net.shuu.mod.util.VillagerProfessionHelper;

import java.util.*;

public class ItemSelectionScreen extends Screen {
  private final Villager villager;
  private final ResourceKey<VillagerProfession> profession;
  private final List<Item> availableItems;
  private final Set<Item> selectedItems;

  private static final int ITEMS_PER_ROW = 9;
  private static final int ITEM_SIZE = 20;
  private static final int ITEM_SPACING = 24;

  private int scrollOffset = 0;
  private final int maxScroll;

  public ItemSelectionScreen(Villager villager) {
    super(Component.literal("Pilih Item untuk Trade Cycling"));
    this.villager = villager;
    this.profession = villager.getVillagerData().profession().unwrapKey().orElse(VillagerProfession.NONE);

    // Ambil daftar item yang bisa dijual profesi ini
    Map<ResourceKey<VillagerProfession>, List<Item>> tradeables = TradeCyclingConfig.getProfessionTradeables();
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

    this.addRenderableWidget(Button.builder(Component.literal("⚙"), button -> {
      if (this.minecraft != null) {
        this.minecraft.setScreenAndShow(new CyclingSpeedConfigScreen(this));
      }
    }).bounds(this.width - 34, 10, 24, 20).build());

    int buttonY = this.height - 40;
    int buttonWidth = 100;
    int buttonSpacing = 10;

    // Tombol Start Cycling
    this.addRenderableWidget(Button.builder(Component.literal("Start Cycling"), button -> {
      if (!selectedItems.isEmpty()) {
        TradeCyclingConfig.getInstance().setDesiredItems(profession, selectedItems);
        TradeCyclingManager.getInstance().startCycling(villager);
        this.onClose();
        TradeCyclingManager.getInstance().openTradeScreenAfterSelection(villager);
      } else if (this.minecraft != null && this.minecraft.player != null) {
        this.minecraft.player.sendOverlayMessage(Component.literal("Pilih minimal satu item terlebih dahulu."));
      }
    })
        .bounds(this.width / 2 - buttonWidth - buttonSpacing / 2, buttonY, buttonWidth, 20)
        .build());

    // Tombol Cancel
    this.addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> {
      this.onClose();
    })
        .bounds(this.width / 2 + buttonSpacing / 2, buttonY, buttonWidth, 20)
        .build());

    // Tombol Clear All
    this.addRenderableWidget(Button.builder(Component.literal("Clear All"), button -> {
      selectedItems.clear();
    })
        .bounds(10, this.height - 40, 80, 20)
        .build());

    // Tombol Select All
    this.addRenderableWidget(Button.builder(Component.literal("Select All"), button -> {
      selectedItems.clear();
      selectedItems.addAll(availableItems);
    })
        .bounds(100, this.height - 40, 80, 20)
        .build());
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
    // Screen.extractRenderState also extracts the background. Calling
    // extractBackground separately would request the blur twice in one frame.
    super.extractRenderState(context, mouseX, mouseY, delta);

    // Title
    context.centeredText(this.font, this.title,
        this.width / 2, 15, 0xFFFFFF);

    // Profession name
    String professionName = VillagerProfessionHelper.getProfessionDisplayName(profession);
    context.centeredText(this.font,
        Component.literal("Profesi: " + professionName),
        this.width / 2, 30, 0xFFFF00);

    // Selected items count
    context.text(this.font,
        Component.literal("§aDipilih: " + selectedItems.size() + "/" + availableItems.size()),
        10, 10, 0xFFFFFF);

    // Item grid
    renderItemGrid(context, mouseX, mouseY);

    // Instructions
    context.centeredText(this.font,
        Component.literal("§7Klik item untuk memilih/membatalkan pilihan"),
        this.width / 2, this.height - 60, 0xFFFFFF);
  }

  private void renderItemGrid(GuiGraphicsExtractor context, int mouseX, int mouseY) {
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
        context.item(new ItemStack(item), x + 2, y + 2);

        // Highlight on hover
        if (mouseX >= x && mouseX < x + ITEM_SIZE && mouseY >= y && mouseY < y + ITEM_SIZE) {
          context.fill(x, y, x + ITEM_SIZE, y + ITEM_SIZE, 0x80FFFFFF);

          // Draw tooltip
          context.setTooltipForNextFrame(this.font, item.getName(new ItemStack(item)), mouseX, mouseY);
        }
      }
    }
  }

  @Override
  public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
    double mouseX = event.x();
    double mouseY = event.y();
    if (event.button() == 0) { // Left click
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

    return super.mouseClicked(event, doubleClick);
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
  public boolean isPauseScreen() {
    return true;
  }
}

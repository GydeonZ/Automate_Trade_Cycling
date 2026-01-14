package net.shuu.mod.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.village.VillagerProfession;
import net.shuu.mod.config.EnchantmentWithLevel;
import net.shuu.mod.config.TradeCyclingConfig;
import net.shuu.mod.manager.TradeCyclingManager;
import net.shuu.mod.util.VillagerProfessionHelper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Screen untuk memilih enchantment spesifik yang diinginkan
 * Khusus untuk profesi Librarian yang menjual Enchanted Books
 */
public class EnchantmentSelectionScreen extends Screen {
  private final VillagerEntity villager;
  private final VillagerProfession profession;

  // Data class untuk enchantment dengan max level
  private static class EnchantmentInfo {
    final RegistryKey<Enchantment> key;
    final int maxLevel;

    EnchantmentInfo(RegistryKey<Enchantment> key, int maxLevel) {
      this.key = key;
      this.maxLevel = maxLevel;
    }
  }

  private final List<EnchantmentInfo> availableEnchantments;
  private final Set<EnchantmentWithLevel> selectedEnchantments;
  private List<EnchantmentInfo> filteredEnchantments;
  private TextFieldWidget searchField;

  private static final int ITEMS_PER_ROW = 7;
  private static final int ITEM_SIZE = 20;
  private static final int ITEM_SPACING = 24;

  private int scrollOffset = 0;
  private int maxScroll;

  public EnchantmentSelectionScreen(VillagerEntity villager) {
    super(Text.literal("Pilih Enchantment untuk Trade Cycling"));
    this.villager = villager;
    this.profession = villager.getVillagerData().getProfession();

    // Daftar enchantment dengan max level yang sering dijual Librarian
    this.availableEnchantments = Arrays.asList(
        new EnchantmentInfo(Enchantments.MENDING, 1),
        new EnchantmentInfo(Enchantments.UNBREAKING, 3),
        new EnchantmentInfo(Enchantments.PROTECTION, 4),
        new EnchantmentInfo(Enchantments.FEATHER_FALLING, 4),
        new EnchantmentInfo(Enchantments.BLAST_PROTECTION, 4),
        new EnchantmentInfo(Enchantments.PROJECTILE_PROTECTION, 4),
        new EnchantmentInfo(Enchantments.FIRE_PROTECTION, 4),
        new EnchantmentInfo(Enchantments.THORNS, 3),
        new EnchantmentInfo(Enchantments.RESPIRATION, 3),
        new EnchantmentInfo(Enchantments.AQUA_AFFINITY, 1),
        new EnchantmentInfo(Enchantments.DEPTH_STRIDER, 3),
        new EnchantmentInfo(Enchantments.FROST_WALKER, 2),
        new EnchantmentInfo(Enchantments.SOUL_SPEED, 3),
        new EnchantmentInfo(Enchantments.SWIFT_SNEAK, 3),
        new EnchantmentInfo(Enchantments.SHARPNESS, 5),
        new EnchantmentInfo(Enchantments.SMITE, 5),
        new EnchantmentInfo(Enchantments.BANE_OF_ARTHROPODS, 5),
        new EnchantmentInfo(Enchantments.KNOCKBACK, 2),
        new EnchantmentInfo(Enchantments.FIRE_ASPECT, 2),
        new EnchantmentInfo(Enchantments.LOOTING, 3),
        new EnchantmentInfo(Enchantments.SWEEPING_EDGE, 3),
        new EnchantmentInfo(Enchantments.EFFICIENCY, 5),
        new EnchantmentInfo(Enchantments.SILK_TOUCH, 1),
        new EnchantmentInfo(Enchantments.FORTUNE, 3),
        new EnchantmentInfo(Enchantments.POWER, 5),
        new EnchantmentInfo(Enchantments.PUNCH, 2),
        new EnchantmentInfo(Enchantments.FLAME, 1),
        new EnchantmentInfo(Enchantments.INFINITY, 1),
        new EnchantmentInfo(Enchantments.LUCK_OF_THE_SEA, 3),
        new EnchantmentInfo(Enchantments.LURE, 3),
        new EnchantmentInfo(Enchantments.LOYALTY, 3),
        new EnchantmentInfo(Enchantments.IMPALING, 5),
        new EnchantmentInfo(Enchantments.RIPTIDE, 3),
        new EnchantmentInfo(Enchantments.CHANNELING, 1),
        new EnchantmentInfo(Enchantments.MULTISHOT, 1),
        new EnchantmentInfo(Enchantments.QUICK_CHARGE, 3),
        new EnchantmentInfo(Enchantments.PIERCING, 4));

    // Load enchantment yang sudah dipilih sebelumnya
    this.selectedEnchantments = new HashSet<>(TradeCyclingConfig.getInstance().getDesiredEnchantments(profession));

    // Inisialisasi filtered list
    this.filteredEnchantments = new ArrayList<>(availableEnchantments);

    // Hitung max scroll
    updateMaxScroll();
  }

  private void updateMaxScroll() {
    int rows = (int) Math.ceil((double) filteredEnchantments.size() / ITEMS_PER_ROW);
    this.maxScroll = Math.max(0, rows - 5);

    // Reset scroll jika melebihi
    if (scrollOffset > maxScroll) {
      scrollOffset = maxScroll;
    }
  }

  @Override
  protected void init() {
    super.init();

    // Search field
    searchField = new TextFieldWidget(this.textRenderer, this.width / 2 - 110, 30, 180, 20, Text.literal("Search"));
    searchField.setPlaceholder(Text.literal("\u00a77Cari enchantment..."));
    searchField.setChangedListener(this::onSearchTextChanged);
    searchField.setFocusUnlocked(true);
    this.addSelectableChild(searchField);
    this.setInitialFocus(searchField);

    // Speed settings button (gear icon)
    this.addDrawableChild(ButtonWidget.builder(Text.literal("\u2699"), button -> {
      // Open speed config screen
      if (this.client != null) {
        this.client.setScreen(new CyclingSpeedConfigScreen(this));
      }
    })
        .dimensions(this.width / 2 + 75, 30, 20, 20)
        .build());

    int buttonY = this.height - 40;
    int buttonWidth = 100;
    int buttonSpacing = 10;

    // Tombol Start Cycling
    this.addDrawableChild(ButtonWidget.builder(Text.literal("Start Cycling"), button -> {
      if (!selectedEnchantments.isEmpty()) {
        TradeCyclingConfig.getInstance().setDesiredEnchantments(profession, selectedEnchantments);
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
      selectedEnchantments.clear();
    })
        .dimensions(10, this.height - 40, 80, 20)
        .build());

    // Tombol Select All
    this.addDrawableChild(ButtonWidget.builder(Text.literal("Select All"), button -> {
      selectedEnchantments.clear();
      // Add all enchantments with their max level (accept any level 1 to max)
      for (EnchantmentInfo info : filteredEnchantments) {
        selectedEnchantments.add(new EnchantmentWithLevel(info.key, 1, info.maxLevel));
      }
    })
        .dimensions(100, this.height - 40, 80, 20)
        .build());
  }

  private void onSearchTextChanged(String searchText) {
    if (searchText == null || searchText.trim().isEmpty()) {
      filteredEnchantments = new ArrayList<>(availableEnchantments);
    } else {
      String search = searchText.toLowerCase().trim();
      filteredEnchantments = availableEnchantments.stream()
          .filter(info -> {
            // Get enchantment name dari key
            String keyPath = info.key.getValue().getPath();
            return keyPath.toLowerCase().contains(search);
          })
          .collect(Collectors.toList());
    }
    updateMaxScroll();
  }

  @Override
  public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    // Render background
    this.renderBackground(context, mouseX, mouseY, delta);

    // Render widgets (buttons)
    super.render(context, mouseX, mouseY, delta);

    // Title
    context.drawCenteredTextWithShadow(this.textRenderer, this.title,
        this.width / 2, 10, 0xFFFFFF);

    // Selected enchantments count
    context.drawTextWithShadow(this.textRenderer,
        Text.literal("\u00a7aDipilih: " + selectedEnchantments.size() + "/" + filteredEnchantments.size() + " (Total: "
            + availableEnchantments.size() + ")"),
        10, 10, 0xFFFFFF);

    // Enchantment grid
    renderEnchantmentGrid(context, mouseX, mouseY);

    // Instructions
    context.drawCenteredTextWithShadow(this.textRenderer,
        Text.literal("§7Klik enchantment untuk memilih/membatalkan pilihan"),
        this.width / 2, this.height - 60, 0xFFFFFF);

    // Render search field last so it's on top
    searchField.render(context, mouseX, mouseY, delta);
  }

  private void renderEnchantmentGrid(DrawContext context, int mouseX, int mouseY) {
    int startX = this.width / 2 - (ITEMS_PER_ROW * ITEM_SPACING) / 2;
    int startY = 60;

    MinecraftClient client = MinecraftClient.getInstance();
    if (client.world == null)
      return;

    for (int row = scrollOffset; row < Math.min(scrollOffset + 5,
        (int) Math.ceil((double) filteredEnchantments.size() / ITEMS_PER_ROW)); row++) {
      for (int col = 0; col < ITEMS_PER_ROW; col++) {
        int enchantIndex = row * ITEMS_PER_ROW + col;
        if (enchantIndex >= filteredEnchantments.size())
          break;

        EnchantmentInfo enchantInfo = filteredEnchantments.get(enchantIndex);
        int x = startX + col * ITEM_SPACING;
        int y = startY + (row - scrollOffset) * ITEM_SPACING;

        // Check if this enchantment is selected (any level)
        boolean isSelected = selectedEnchantments.stream()
            .anyMatch(e -> e.getEnchantment().equals(enchantInfo.key));

        // Draw slot background
        int color = isSelected ? 0xFF00FF00 : 0xFF8B8B8B;
        context.fill(x - 1, y - 1, x + ITEM_SIZE + 1, y + ITEM_SIZE + 1, color);
        context.fill(x, y, x + ITEM_SIZE, y + ITEM_SIZE, 0xFF373737);

        // Create enchanted book with this enchantment at max level
        Optional<RegistryEntry.Reference<Enchantment>> enchantmentEntry = client.world.getRegistryManager()
            .get(RegistryKeys.ENCHANTMENT).getEntry(enchantInfo.key);

        if (enchantmentEntry.isPresent()) {
          ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
          ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(
              ItemEnchantmentsComponent.DEFAULT);
          builder.add(enchantmentEntry.get(), enchantInfo.maxLevel);
          book.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());

          // Draw item
          context.drawItem(book, x + 2, y + 2);

          // Highlight on hover
          if (mouseX >= x && mouseX < x + ITEM_SIZE && mouseY >= y && mouseY < y + ITEM_SIZE) {
            context.fill(x, y, x + ITEM_SIZE, y + ITEM_SIZE, 0x80FFFFFF);

            // Draw tooltip with all level ranges
            java.util.List<Text> tooltip = new java.util.ArrayList<>();
            String baseName = enchantmentEntry.get().getIdAsString().replace("minecraft:", "");

            // Add all levels
            for (int level = 1; level <= enchantInfo.maxLevel; level++) {
              Text leveledName = Enchantment.getName(enchantmentEntry.get(), level);
              tooltip.add(leveledName);
            }

            context.drawTooltip(this.textRenderer, tooltip, mouseX, mouseY);
          }
        }
      }
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    // Check search field first
    if (searchField.mouseClicked(mouseX, mouseY, button)) {
      return true;
    }

    if (button == 0) { // Left click
      int startX = this.width / 2 - (ITEMS_PER_ROW * ITEM_SPACING) / 2;
      int startY = 60;

      for (int row = scrollOffset; row < Math.min(scrollOffset + 5,
          (int) Math.ceil((double) filteredEnchantments.size() / ITEMS_PER_ROW)); row++) {
        for (int col = 0; col < ITEMS_PER_ROW; col++) {
          int enchantIndex = row * ITEMS_PER_ROW + col;
          if (enchantIndex >= filteredEnchantments.size())
            break;

          int x = startX + col * ITEM_SPACING;
          int y = startY + (row - scrollOffset) * ITEM_SPACING;

          if (mouseX >= x && mouseX < x + ITEM_SIZE && mouseY >= y && mouseY < y + ITEM_SIZE) {
            EnchantmentInfo enchantInfo = filteredEnchantments.get(enchantIndex);

            // Check if already selected
            Optional<EnchantmentWithLevel> existing = selectedEnchantments.stream()
                .filter(e -> e.getEnchantment().equals(enchantInfo.key))
                .findFirst();

            if (existing.isPresent()) {
              // Remove if already selected
              selectedEnchantments.remove(existing.get());
            } else {
              // Add with full level range (1 to max) - accept any level
              selectedEnchantments.add(new EnchantmentWithLevel(enchantInfo.key, 1, enchantInfo.maxLevel));
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
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (searchField.keyPressed(keyCode, scanCode, modifiers)) {
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char chr, int modifiers) {
    if (searchField.charTyped(chr, modifiers)) {
      return true;
    }
    return super.charTyped(chr, modifiers);
  }

  @Override
  public boolean shouldPause() {
    return true;
  }
}

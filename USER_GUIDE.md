# VillagerTrade Pro - User Guide

## 📋 Table of Contents
1. [Installation](#installation)
2. [How to Use](#how-to-use)
3. [Professions and Items](#professions-and-items)
4. [Tips and Tricks](#tips-and-tricks)
5. [Troubleshooting](#troubleshooting)

## 🔧 Installation

1. Make sure you have **Fabric Loader** and **Fabric API** installed
2. Download the `.jar` file from `build/libs/`
3. Place the `.jar` file in your Minecraft `mods` folder
4. Launch Minecraft with Fabric profile

## 🎮 How to Use

### Step 1: Villager Preparation
- Make sure the villager you're targeting:
  - ✅ Has a profession (not Nitwit or Unemployed)
  - ✅ Has never been traded with (Novice level)
  - ✅ Has access to their job site block
  - ✅ Their trade offers are not locked

### Step 2: Selecting Desired Items

1. **Point your crosshair** at the villager you want to cycle
2. Press **V** (default)
3. The GUI will open showing all items that profession can trade
4. **Click on items** you want (selected items will have a green border)
5. You can select multiple items at once

**GUI Buttons:**
- `Select All` - Select all items
- `Clear All` - Clear all selections
- `Start Cycling` - Begin the cycling process
- `Cancel` - Close GUI without starting

### Step 3: Selecting Enchantments (For Enchanted Books)

When you select "Enchanted Book":
1. An enchantment selection screen will appear with 37+ enchantments
2. Use the **search field** at the top to quickly find enchantments
3. Click on enchantments to select them (green highlight)
4. You can select multiple enchantments
5. Click the **gear icon (⚙)** to configure cycling speed (in milliseconds)
6. Click **Done** to confirm your selections

**Available Enchantments:**
- Combat: Sharpness, Smite, Bane of Arthropods, Knockback, Fire Aspect, Looting, Sweeping Edge
- Defense: Protection, Fire Protection, Blast Protection, Projectile Protection, Feather Falling, Thorns
- Tools: Efficiency, Fortune, Silk Touch, Unbreaking, Mending
- Bow: Power, Punch, Flame, Infinity
- Fishing: Luck of the Sea, Lure
- Trident: Loyalty, Impaling, Riptide, Channeling
- And more!

### Step 4: Starting Cycling

After selecting items, there are 2 ways to start:
- **Method 1**: Click the `Start Cycling` button in the GUI
- **Method 2**: Close the GUI and press **C** while targeting the villager

The mod will:
1. ✨ Start monitoring the villager's trade offers
2. 🔄 Automatically check every trade change
3. 🎯 Stop when it finds the item you're looking for
4. 💬 Send a notification in chat

### Step 5: Stopping Cycling

To manually stop cycling:
- Press **C** again
- Or quit the game/change dimensions

## 📦 Professions and Items

### Armorer (Blast Furnace)
- Armor: Iron, Chainmail, Diamond sets
- Other: Shield, Bell

### Butcher (Smoker)
- Cooked meats: Chicken, Porkchop, Mutton, Rabbit, Beef
- Rabbit Stew

### Cartographer (Cartography Table)
- Maps, Filled Maps
- Item Frame, Painting
- Globe Banner Pattern

### Cleric (Brewing Stand)
- Redstone, Lapis Lazuli, Glowstone
- Ender Pearl
- Bottle o' Enchanting
- Brewing Stand

### Farmer (Composter)
- Food items: Bread, Apple, Cookie, Cake, Pumpkin Pie
- Suspicious Stew
- Golden Carrot, Glistering Melon Slice

### Fisherman (Barrel)
- Fish: Cod, Salmon, Tropical Fish, Pufferfish
- Cooked variants
- Fishing Rod

### Fletcher (Fletching Table)
- Arrow, Tipped Arrow
- Bow, Crossbow
- Flint, Tripwire Hook

### Leatherworker (Cauldron)
- Leather items
- Leather Horse Armor
- Saddle

### Librarian (Lectern)
- **Enchanted Books** ⭐ (with level selection)
- Bookshelf
- Clock, Compass
- Glass, Name Tag, Lantern

### Mason (Stonecutter)
- Brick, Clay Ball
- Polished stones (Andesite, Diorite, Granite)
- Quartz, Terracotta
- Glazed Terracotta

### Shepherd (Loom)
- Various colored wool
- Bed, Banner, Painting

### Toolsmith (Smithing Table)
- Iron tools: Axe, Pickaxe, Shovel, Hoe
- Diamond tools: Axe, Pickaxe, Shovel, Hoe
- Bell

### Weaponsmith (Grindstone)
- Iron weapons: Sword, Axe
- Diamond weapons: Sword, Axe
- Bell, Enchanted Book

## 💡 Tips and Tricks

### 1. Finding Enchanted Books
- Use **Librarian** for the best enchanted books
- Set up multiple Librarians at once
- Select the enchantment you want in the GUI
- Cycle until you find the right one!

### 2. Cycling Optimization
- Make sure villagers can easily access their job site
- Don't let other villagers claim the same job site
- Place villagers in enclosed areas for faster cycling

### 3. Trading Station
- Build a dedicated trading hall for cycling
- One villager per cell
- Job site in the same cell
- Easy to cycle many villagers

### 4. Batch Processing
- Cycle multiple villagers simultaneously for different professions
- Save item selections for each profession
- The mod remembers your choices per profession

### 5. Configurable Speed
- Click the gear icon (⚙) in the enchantment selection screen
- Adjust cycling speed in milliseconds (default: 1000ms = 1 second)
- Lower values = faster cycling (minimum: 50ms)
- Higher values = slower cycling (maximum: 10000ms)

## 🔧 Troubleshooting

### Cycling not working
**Possible causes:**
- ❌ Villager has been traded with (locked trades)
- ❌ Villager doesn't have access to job site
- ❌ Job site is claimed by another villager
- ❌ Villager is Nitwit or Unemployed

**Solutions:**
- ✅ Use a new villager that has never been traded with
- ✅ Make sure there's a job site block within range
- ✅ Break and replace the job site if needed

### GUI not appearing
**Possible causes:**
- ❌ Not targeting villager correctly
- ❌ Villager doesn't have a valid profession

**Solutions:**
- ✅ Make sure crosshair is pointing directly at the villager
- ✅ Check villager profession (must have a job)

### Item not found
**Possible causes:**
- ❌ Item might be at a higher trade level
- ❌ Unlucky RNG

**Solutions:**
- ✅ Be patient, cycling takes time
- ✅ Try with another villager
- ✅ Make sure the item exists for that profession

### Search field not clickable
**Solutions:**
- ✅ Click directly on the text field
- ✅ The field is rendered on top layer for better interaction
- ✅ Make sure you're not clicking on an item instead

### Mod crash or error
**Solutions:**
- ✅ Check Fabric API version matches requirements
- ✅ Make sure there are no conflicting mods
- ✅ Check logs for detailed errors
- ✅ Report bugs on GitHub Issues

## ⚙️ Keybind Configuration

To change keybinds:
1. Open **Options** → **Controls**
2. Find the **VillagerTrade Pro** category
3. Change keybinds as desired:
   - `Open Item Selection` (default: V)
   - `Toggle Trade Cycling` (default: C)

## 📝 Important Notes

1. ⚠️ **Traded villagers cannot be cycled!**
   - Trade offers become permanently locked after the first trade
   - Use new villagers for cycling

2. 📊 **Success rate depends on RNG**
   - No guarantee the item will be found immediately
   - The more items in the pool, the longer cycling takes

3. 🌐 **Multiplayer compatibility**
   - This mod is client-side
   - Requires permission on servers to manipulate villagers
   - Some servers may ban/restrict trade cycling

4. 💾 **Item selections saved per profession**
   - No need to set up every time
   - Selections apply to all villagers of the same profession
   - Restarting the game doesn't delete selections

5. 🎯 **Enchantment level selection**
   - You can specify enchantment level ranges
   - Tooltip shows all available levels (e.g., Efficiency I, II, III, IV, V)
   - Cycling stops only when the correct level is found

## 🎯 Best Practices

1. **Set up a trading hall** with separate compartments
2. **Label each villager** with name tags for tracking
3. **Keep backup villagers** for important professions
4. **Document good trades** for reference
5. **Be patient** - cycling takes time but it's worth it!

---

**Happy Trading!** 🎉

If you have questions or issues, please open a GitHub Issue or contact the developer.

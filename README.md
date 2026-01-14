# VillagerTrade Pro

A Minecraft Fabric mod that allows you to automatically cycle villager trades until you find the items you want.

## Features

- **Automatic Cycling**: The mod continuously cycles villager trades until it finds the item you're looking for
- **Item Selection GUI**: Easy-to-use interface to select items based on villager profession
- **All Professions Supported**: Works with all villager professions (Armorer, Butcher, Cartographer, etc.)
- **Convenient Keybinds**: Quick access with keyboard shortcuts
- **Enchantment Selection**: Advanced GUI for selecting specific enchantments with level ranges
- **Search Functionality**: Quickly find items and enchantments with built-in search
- **Configurable Speed**: Customize cycling speed to match your preferences

## How to Use

### 1. Opening the Item Selection GUI

1. Point your crosshair at a villager with a profession
2. Press **V** (default key)
3. The GUI will display all items that the villager's profession can trade

### 2. Selecting Desired Items

In the GUI:
- **Click on items** to select/deselect them
- **Select All**: Select all items at once
- **Clear All**: Clear all selections
- Selected items will have a green border

### 3. Selecting Enchantments (For Enchanted Books)

When you select "Enchanted Book":
1. An enchantment selection screen will appear
2. Use the **search field** to quickly find enchantments
3. Click on enchantments to select them
4. Selected enchantments will be highlighted in green
5. Click the **gear icon (⚙)** to configure cycling speed

### 4. Starting Trade Cycling

After selecting items:
- Click the **Start Cycling** button in the GUI, or
- Press **C** (default key) while targeting the villager

### 5. Stopping Cycling

- Press **C** again to manually stop cycling
- Cycling will automatically stop when the desired item is found

## Keybinds

| Key | Function | Customizable |
|-----|----------|--------------|
| V | Open item selection GUI | Yes |
| C | Start/Stop trade cycling | Yes |

To change keybinds, go to **Options > Controls > Key Binds** and find the "VillagerTrade Pro" category.

## Supported Professions

This mod supports trade cycling for the following professions:

- **Armorer**: Various armor types (Iron, Chainmail, Diamond), Shield, Bell
- **Butcher**: Cooked meats (Chicken, Porkchop, Mutton, Rabbit, Beef), Rabbit Stew
- **Cartographer**: Maps, Item Frame, Globe Banner Pattern, Painting
- **Cleric**: Redstone, Lapis Lazuli, Glowstone, Ender Pearl, Bottle o' Enchanting, Brewing Stand
- **Farmer**: Bread, Apple, Cookie, Cake, Pumpkin Pie, Suspicious Stew, Golden Carrot, Glistering Melon
- **Fisherman**: Fish (Cod, Salmon, Tropical, Pufferfish), Fishing Rod
- **Fletcher**: Arrow, Tipped Arrow, Bow, Crossbow, Flint, Tripwire Hook
- **Leatherworker**: Leather, Leather Armor, Leather Horse Armor, Saddle
- **Librarian**: Bookshelf, Enchanted Book, Clock, Compass, Glass, Name Tag, Lantern
- **Mason**: Brick, Clay, Dripstone Block, Polished stones, Quartz, Terracotta
- **Shepherd**: Various colored wool, Bed, Painting, Banner
- **Toolsmith**: Iron/Diamond tools (Axe, Pickaxe, Shovel, Hoe), Bell
- **Weaponsmith**: Iron/Diamond weapons (Sword, Axe), Bell, Enchanted Book

## How It Works

1. The mod saves a list of items you want for each profession
2. When cycling starts, the mod continuously checks the villager's trade offers
3. If any item matches your list, cycling stops and sends a notification
4. You can then immediately trade with the villager

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.18.4 or newer
- Fabric API
- Java 21 or newer

## Tips

- Make sure the villager has access to their job site block
- Villager must not have been traded with before for cycling to work
- For best results, use novice villagers (level 1)
- This mod works in both singleplayer and multiplayer (with permission)

## Important Notes

⚠️ **Warning**: Trade cycling only works on villagers that have never been traded with. Once a villager has been traded with once, their trade offers become locked and cannot be cycled anymore.

## Building the Mod

```bash
./gradlew build
```

The mod file will be located in `build/libs/`

## License

CC0-1.0 License

## Credits

Built with Fabric API

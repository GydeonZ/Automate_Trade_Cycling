# 📦 Project Structure - Automate Trade Cycling

## 🏗️ Struktur File

```
Automate Trade Cycling/
│
├── 📄 build.gradle              # Gradle build configuration
├── 📄 gradle.properties         # Project version & dependencies
├── 📄 settings.gradle           # Gradle settings
├── 📄 gradlew, gradlew.bat     # Gradle wrapper scripts
├── 📄 LICENSE                   # CC0-1.0 License
│
├── 📚 Documentation/
│   ├── README.md               # Main documentation (English)
│   ├── PANDUAN_INDONESIA.md    # Panduan lengkap (Bahasa Indonesia)
│   ├── QUICKSTART.md           # Quick start guide
│   ├── CHANGELOG.md            # Version history & changes
│   └── PROJECT_STRUCTURE.md    # This file
│
├── 🔧 gradle/wrapper/          # Gradle wrapper files
│
├── 🏃 run/                     # Minecraft run directory
│
├── 🏭 build/                   # Build output
│   └── libs/
│       ├── automate-trade-cycling-1.0.0.jar         # Main mod file
│       └── automate-trade-cycling-1.0.0-sources.jar # Source code
│
└── 💻 src/main/
    ├── java/net/shuu/mod/
    │   │
    │   ├── 📝 AutomateTradeCycling.java
    │   │   └── Main mod class, mod initialization
    │   │
    │   ├── 📝 AutomateTradeCyclingClient.java
    │   │   └── Client-side initialization
    │   │       ├── Register keybindings
    │   │       └── Setup tick events
    │   │
    │   ├── 📝 AutomateTradeCyclingDataGenerator.java
    │   │   └── Data generation (if needed)
    │   │
    │   ├── 📁 config/
    │   │   └── 📝 TradeCyclingConfig.java
    │   │       ├── Stores desired items per profession
    │   │       ├── Cycling state management
    │   │       └── Complete tradeable items database
    │   │
    │   ├── 📁 manager/
    │   │   └── 📝 TradeCyclingManager.java
    │   │       ├── Core cycling logic
    │   │       ├── Trade offer checking
    │   │       ├── Villager targeting
    │   │       └── Tick-based processing
    │   │
    │   ├── 📁 screen/
    │   │   └── 📝 ItemSelectionScreen.java
    │   │       ├── Interactive GUI for item selection
    │   │       ├── Grid-based item display
    │   │       ├── Click to select/deselect
    │   │       ├── Scroll support
    │   │       └── Select All / Clear All buttons
    │   │
    │   ├── 📁 keybind/
    │   │   └── 📝 ModKeyBindings.java
    │   │       ├── Register custom keybindings
    │   │       ├── V: Open item selection GUI
    │   │       ├── C: Toggle cycling on/off
    │   │       └── Key press event handlers
    │   │
    │   ├── 📁 util/
    │   │   └── 📝 VillagerProfessionHelper.java
    │   │       ├── Profession to job site mapping
    │   │       ├── Display name utilities
    │   │       └── Job site validation
    │   │
    │   └── 📁 mixin/
    │       ├── 📝 VillagerEntityMixin.java
    │       │   └── Hook into villager tick for monitoring
    │       └── 📝 ExampleMixin.java
    │           └── Example mixin (not used)
    │
    └── resources/
        ├── 📄 fabric.mod.json
        │   └── Mod metadata & entrypoints
        │
        ├── 📄 automate-trade-cycling.mixins.json
        │   └── Mixin configuration
        │
        └── assets/automate-trade-cycling/
            ├── icon.png                    # Mod icon
            └── lang/
                └── en_us.json             # English translations

```

## 🎯 Core Components

### 1. Configuration System
**File:** `TradeCyclingConfig.java`
- Singleton pattern untuk global state
- Map<VillagerProfession, Set<Item>> untuk desired items
- Static method untuk tradeable items database
- Per-profession memory

### 2. Cycling Manager
**File:** `TradeCyclingManager.java`
- Core business logic
- Tick-based processing (0.5s delay)
- Trade offer validation
- Villager targeting system
- Start/stop cycling control

### 3. User Interface
**File:** `ItemSelectionScreen.java`
- Grid layout (9 items per row)
- Visual selection (green border)
- Tooltip on hover
- Scroll support
- Action buttons

### 4. Keybind System
**File:** `ModKeyBindings.java`
- V: Open GUI
- C: Toggle cycling
- Fabric Key Binding API integration
- Client tick event handling

### 5. Utilities
**File:** `VillagerProfessionHelper.java`
- 13 professions mapped to job sites
- Display name conversions
- Profession validation

## 🔧 Technical Details

### Dependencies
```gradle
Minecraft: 1.21.1
Fabric Loader: >=0.18.4
Fabric API: Latest
Java: 21+
```

### Mixins
```json
VillagerEntityMixin:
  - Target: VillagerEntity.tick()
  - Purpose: Monitor villager state changes
  - Injection: TAIL
```

### Client Events
```java
ClientTickEvents.END_CLIENT_TICK:
  - Keybind processing
  - Cycling manager tick
  - UI updates
```

## 📊 Data Flow

```
User Input (Keybind)
    ↓
ModKeyBindings
    ↓
[Open GUI] → ItemSelectionScreen → Select Items → TradeCyclingConfig
    ↓
[Start Cycle] → TradeCyclingManager
    ↓
Tick Event Loop:
  ├─ Check villager alive?
  ├─ Check trade offers
  ├─ Compare with desired items
  └─ Found? → Stop & Notify
```

## 🎨 GUI Layout

```
┌─────────────────────────────────────────┐
│  Title: Pilih Item untuk Trade Cycling │
├─────────────────────────────────────────┤
│  Selected: X/Y        Profesi: Librarian│
├─────────────────────────────────────────┤
│  [📦] [📦] [📦] [📦] [📦] [📦] [📦] [📦] [📦] │
│  [📦] [✅] [📦] [✅] [📦] [📦] [✅] [📦] [📦] │
│  [📦] [📦] [📦] [📦] [📦] [📦] [📦] [📦] [📦] │
│  [📦] [📦] [📦] [📦] [📦] [📦] [📦] [📦] [📦] │
├─────────────────────────────────────────┤
│  Klik item untuk memilih/batalkan       │
├─────────────────────────────────────────┤
│ [Clear All] [Select All]                │
│             [Start] [Cancel]            │
└─────────────────────────────────────────┘

Legend:
📦 = Unselected item
✅ = Selected item (green border)
```

## 🔄 Cycling Process

```
1. User selects items in GUI
   ↓
2. Items saved to TradeCyclingConfig
   ↓
3. User starts cycling (button/keybind)
   ↓
4. TradeCyclingManager.startCycling()
   - Set target villager
   - Enable cycling flag
   ↓
5. Every tick (0.5s delay):
   - Check villager valid
   - Get trade offers
   - Compare with desired items
   ↓
6. If match found:
   - Stop cycling
   - Send chat notification
   - Highlight item
   ↓
7. If not found:
   - Continue cycling
   - Villager refreshes naturally via jobsite
```

## 🧪 Testing Checklist

- [ ] All professions recognized
- [ ] GUI opens with correct items
- [ ] Item selection works (click toggle)
- [ ] Cycling starts properly
- [ ] Cycling stops when item found
- [ ] Notifications appear in chat
- [ ] Keybinds work correctly
- [ ] No conflicts with other mods
- [ ] Multiplayer compatible
- [ ] Performance acceptable

## 📈 Future Enhancements

### v1.1.0
- Config file (TOML/JSON)
- Adjustable cycling speed
- Sound notifications
- Statistics tracking

### v1.2.0
- Visual particle effects
- Batch cycling support
- Enchantment filtering
- Auto jobsite manipulation

### v2.0.0
- AI optimization
- Trading hall integration
- Server-side support
- Advanced analytics

## 🤝 Contributing

Ingin berkontribusi? Check out:
1. Fork repository
2. Create feature branch
3. Make changes
4. Test thoroughly
5. Submit pull request

## 📞 Support

- GitHub Issues: Report bugs/features
- Discussions: Ask questions
- Wiki: Detailed guides

---

**Built with ❤️ using Fabric API**

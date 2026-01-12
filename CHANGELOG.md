# Changelog

All notable changes to the Automate Trade Cycling mod will be documented in this file.

## [1.0.0] - 2026-01-12

### Added
- 🎉 Initial release of Automate Trade Cycling mod
- ✨ Automatic trade cycling system for villagers
- 🎨 Interactive GUI for item selection based on villager profession
- ⌨️ Keybindings for quick access (V for GUI, C for toggle cycling)
- 📋 Support for all 13 villager professions:
  - Armorer, Butcher, Cartographer, Cleric
  - Farmer, Fisherman, Fletcher, Leatherworker
  - Librarian, Mason, Shepherd, Toolsmith, Weaponsmith
- 🔍 Smart trade offer detection
- 💬 In-game chat notifications when desired items are found
- 📊 Multi-item selection support
- 🎯 Per-profession item memory/configuration
- 🌐 Multiplayer compatible (client-side)
- 📚 Comprehensive documentation in English and Indonesian

### Features
- Select multiple items to search for at once
- Automatic stop when any selected item is found
- Visual feedback with colored borders in GUI
- Scroll support for long item lists
- Select All / Clear All buttons for convenience
- Crosshair-based villager targeting
- Real-time cycling status updates

### Technical
- Built with Fabric API for Minecraft 1.21.1
- Java 21 compatible
- Mixin integration for villager entity tracking
- Client-side tick events for cycling management
- Optimized performance with configurable delays

### Known Limitations
- Only works with novice (untaded) villagers
- Trade offers lock permanently after first trade
- Requires villager to have access to job site block
- RNG-dependent success rate

---

## Future Plans

### [1.1.0] - Planned
- [ ] Config file support for customizable settings
- [ ] Adjustable cycling speed/delay
- [ ] Sound notifications option
- [ ] Statistics tracking (cycles per item found)
- [ ] Localization for more languages

### [1.2.0] - Planned
- [ ] Visual indicators above villagers being cycled
- [ ] Batch cycling multiple villagers
- [ ] Filter items by enchantment level (for books)
- [ ] Auto-break/place jobsite blocks (if enabled)

### [2.0.0] - Future Vision
- [ ] AI-powered optimal cycling strategies
- [ ] Integration with trading station mods
- [ ] Server-side support with admin controls
- [ ] Trading history and analytics
- [ ] Preset configurations for popular items

---

## Bug Fixes

None yet - this is the initial release!

---

## Contributing

Found a bug or have a feature request? Please open an issue on our GitHub repository:
https://github.com/GydeonZ/Automate_Trade_Cycling/issues

---

**Legend:**
- ✨ New feature
- 🐛 Bug fix
- 🎨 UI/UX improvement
- ⚡ Performance improvement
- 📚 Documentation
- 🔧 Configuration
- 🌐 Multiplayer/Network

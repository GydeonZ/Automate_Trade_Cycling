# Automate Trade Cycling

Mod Minecraft Fabric yang memungkinkan Anda untuk otomatis melakukan trade cycling pada villager sampai menemukan item yang Anda inginkan.

## Fitur

- **Otomatis Cycling**: Mod akan terus cycling trade villager sampai menemukan item yang Anda cari
- **GUI Pemilihan Item**: Interface yang mudah untuk memilih item berdasarkan profesi villager
- **Dukungan Semua Profesi**: Mendukung semua profesi villager (Armorer, Butcher, Cartographer, dll)
- **Keybind Praktis**: Akses cepat dengan shortcut keyboard

## Cara Penggunaan

### 1. Membuka GUI Pemilihan Item

1. Arahkan crosshair Anda ke villager yang memiliki profesi
2. Tekan tombol **V** (default key)
3. GUI akan menampilkan semua item yang bisa dijual oleh profesi villager tersebut

### 2. Memilih Item yang Dicari

Di dalam GUI:
- **Klik item** untuk memilih/membatalkan pilihan
- **Select All**: Pilih semua item
- **Clear All**: Hapus semua pilihan
- Item yang dipilih akan memiliki border hijau

### 3. Memulai Trade Cycling

Setelah memilih item:
- Klik tombol **Start Cycling** di GUI, atau
- Tekan tombol **C** (default key) saat menarget villager

### 4. Menghentikan Cycling

- Tekan tombol **C** lagi untuk menghentikan cycling secara manual
- Cycling akan otomatis berhenti ketika item yang dicari ditemukan

## Keybinds

| Tombol | Fungsi | Dapat Diubah |
|--------|--------|--------------|
| V | Buka GUI pemilihan item | Ya |
| C | Start/Stop trade cycling | Ya |

Untuk mengubah keybind, buka **Options > Controls > Key Binds** dan cari kategori "Automate Trade Cycling".

## Profesi yang Didukung

Mod ini mendukung trade cycling untuk profesi berikut:

- **Armorer**: Armor berbagai jenis (Iron, Chainmail, Diamond), Shield, Bell
- **Butcher**: Cooked meat (Chicken, Porkchop, Mutton, Rabbit, Beef), Rabbit Stew
- **Cartographer**: Maps, Item Frame, Globe Banner Pattern, Painting
- **Cleric**: Redstone, Lapis Lazuli, Glowstone, Ender Pearl, Bottle o' Enchanting, Brewing Stand
- **Farmer**: Bread, Apple, Cookie, Cake, Pumpkin Pie, Suspicious Stew, Golden Carrot, Glistering Melon
- **Fisherman**: Fish (Cod, Salmon, Tropical, Pufferfish), Fishing Rod
- **Fletcher**: Arrow, Tipped Arrow, Bow, Crossbow, Flint, Tripwire Hook
- **Leatherworker**: Leather, Leather Armor, Leather Horse Armor, Saddle
- **Librarian**: Bookshelf, Enchanted Book, Clock, Compass, Glass, Name Tag, Lantern
- **Mason**: Brick, Clay, Dripstone Block, Polished stones, Quartz, Terracotta
- **Shepherd**: Wool berbagai warna, Bed, Painting, Banner
- **Toolsmith**: Iron/Diamond tools (Axe, Pickaxe, Shovel, Hoe), Bell
- **Weaponsmith**: Iron/Diamond weapons (Sword, Axe), Bell, Enchanted Book

## Cara Kerja

1. Mod akan menyimpan daftar item yang Anda inginkan untuk setiap profesi
2. Saat cycling dimulai, mod akan terus memeriksa trade offers villager
3. Jika ada item yang cocok dengan daftar, cycling akan berhenti dan memberi notifikasi
4. Anda kemudian bisa langsung melakukan trade

## Persyaratan

- Minecraft 1.21.1
- Fabric Loader 0.18.4 atau lebih baru
- Fabric API
- Java 21 atau lebih baru

## Tips

- Pastikan villager memiliki akses ke job site block-nya
- Villager harus belum pernah di-trade sebelumnya untuk bisa cycling
- Untuk hasil terbaik, gunakan novice villager (level 1)
- Mod ini bekerja di singleplayer dan multiplayer (dengan permission)

## Catatan Penting

⚠️ **Perhatian**: Trade cycling hanya bekerja pada villager yang belum pernah melakukan trade. Setelah villager di-trade sekali, trade offers-nya akan terkunci dan tidak bisa di-cycle lagi.

## Build Mod

```bash
./gradlew build
```

File mod akan berada di `build/libs/`

## License

CC0-1.0 License

## Credits

Dibuat dengan Fabric API

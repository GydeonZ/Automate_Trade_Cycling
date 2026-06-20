# Panduan Penggunaan Automate Trade Cycling

## 📋 Daftar Isi
1. [Instalasi](#instalasi)
2. [Cara Penggunaan](#cara-penggunaan)
3. [Profesi dan Item](#profesi-dan-item)
4. [Tips dan Trik](#tips-dan-trik)
5. [Troubleshooting](#troubleshooting)

## 🔧 Instalasi

1. Pastikan Anda sudah install **Fabric Loader** dan **Fabric API**
2. Download file `.jar` mod ini dari folder `build/libs/`
3. Letakkan file `.jar` ke folder `mods` di direktori Minecraft Anda
4. Jalankan Minecraft dengan profile Fabric

## 🎮 Cara Penggunaan

### Langkah 1: Persiapan Villager
- Pastikan villager yang Anda target:
  - ✅ Memiliki profesi (bukan Nitwit atau Unemployed)
  - ✅ Belum pernah di-trade sama sekali (Novice level)
  - ✅ Memiliki akses ke job site block-nya
  - ✅ Tidak terkunci (locked) trade offers-nya

### Langkah 2: Memilih Item yang Dicari

1. **Arahkan crosshair** ke villager yang ingin di-cycle
2. Tekan tombol **V** (default)
3. GUI akan terbuka menampilkan semua item yang bisa dijual profesi tersebut
4. **Klik pada item** yang Anda inginkan (akan ada border hijau saat terpilih)
5. Anda bisa pilih beberapa item sekaligus

**Tombol di GUI:**
- `Select All` - Pilih semua item
- `Clear All` - Hapus semua pilihan
- `Start Cycling` - Mulai proses cycling
- `Cancel` - Tutup GUI tanpa memulai

### Langkah 3: Memulai Cycling

Setelah memilih item, ada 2 cara untuk memulai:
- **Cara 1**: Klik tombol `Start Cycling` di GUI
- **Cara 2**: Tutup GUI dan tekan tombol **C** saat menarget villager

Mod akan:
1. ✨ Mulai memonitor trade offers villager
2. 🔄 Secara otomatis mengecek setiap perubahan trade
3. 🎯 Berhenti saat menemukan item yang Anda cari
4. 💬 Memberi notifikasi di chat

### Langkah 4: Menghentikan Cycling

Untuk menghentikan cycling secara manual:
- Tekan tombol **C** lagi
- Atau tutup game/ganti dimensi

## 📦 Profesi dan Item

### Armorer (Blast Furnace)
- Armor: Iron, Chainmail, Diamond sets
- Lainnya: Shield, Bell

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
- **Enchanted Books** ⭐
- Bookshelf
- Clock, Compass
- Glass, Name Tag, Lantern

### Mason (Stonecutter)
- Brick, Clay Ball
- Polished stones (Andesite, Diorite, Granite)
- Quartz, Terracotta
- Glazed Terracotta

### Shepherd (Loom)
- Wool berbagai warna
- Bed, Banner, Painting

### Toolsmith (Smithing Table)
- Iron tools: Axe, Pickaxe, Shovel, Hoe
- Diamond tools: Axe, Pickaxe, Shovel, Hoe
- Bell

### Weaponsmith (Grindstone)
- Iron weapons: Sword, Axe
- Diamond weapons: Sword, Axe
- Bell, Enchanted Book

## 💡 Tips dan Trik

### 1. Mencari Enchanted Books
- Gunakan **Librarian** untuk enchanted books terbaik
- Siapkan beberapa Librarian sekaligus
- Pilih enchantment yang Anda inginkan di GUI
- Cycle sampai ketemu yang pas!

### 2. Optimasi Cycling
- Pastikan villager bisa mengakses job site dengan mudah
- Jangan ada villager lain yang bisa claim job site yang sama
- Tempatkan villager di area tertutup untuk mempercepat

### 3. Trading Station
- Buat trading hall khusus untuk cycling
- Satu villager per cell
- Job site di dalam cell yang sama
- Mudah untuk cycle banyak villager

### 4. Batch Processing
- Cycle beberapa villager sekaligus untuk profesi berbeda
- Simpan pilihan item untuk setiap profesi
- Mod akan ingat pilihan Anda per profesi

## 🔧 Troubleshooting

### Cycling tidak berfungsi
**Kemungkinan penyebab:**
- ❌ Villager sudah pernah di-trade (locked trades)
- ❌ Villager tidak punya akses ke job site
- ❌ Job site sudah di-claim villager lain
- ❌ Villager adalah Nitwit atau Unemployed

**Solusi:**
- ✅ Gunakan villager baru yang belum pernah di-trade
- ✅ Pastikan ada job site block dalam jangkauan
- ✅ Break dan place ulang job site jika perlu

### GUI tidak muncul
**Kemungkinan penyebab:**
- ❌ Tidak menarget villager dengan benar
- ❌ Villager tidak memiliki profesi valid

**Solusi:**
- ✅ Pastikan crosshair tepat mengarah ke villager
- ✅ Cek profesi villager (harus punya job)

### Item tidak ditemukan
**Kemungkinan penyebab:**
- ❌ Item mungkin ada di level trade yang lebih tinggi
- ❌ RNG tidak beruntung

**Solusi:**
- ✅ Bersabar, cycling membutuhkan waktu
- ✅ Coba dengan villager lain
- ✅ Pastikan item yang dicari memang ada di profesi tersebut

### Mod crash atau error
**Solusi:**
- ✅ Cek versi Fabric API sudah sesuai
- ✅ Pastikan tidak ada mod yang conflict
- ✅ Cek log untuk error detail
- ✅ Report bug di GitHub Issues

## ⚙️ Konfigurasi Keybind

Untuk mengubah keybind:
1. Buka **Options** → **Controls**
2. Cari kategori **Automate Trade Cycling**
3. Ubah keybind sesuai keinginan:
   - `Open Item Selection` (default: V)
   - `Toggle Trade Cycling` (default: C)

## 📝 Catatan Penting

1. ⚠️ **Villager yang sudah di-trade tidak bisa di-cycle!**
   - Trade offers akan terkunci permanent setelah trade pertama
   - Gunakan villager baru untuk cycling

2. 📊 **Success rate tergantung RNG**
   - Tidak ada jaminan item langsung ketemu
   - Semakin banyak item di pool, semakin lama cycling

3. 🌐 **Multiplayer compatibility**
   - Mod ini client-side
   - Perlu permission di server untuk memanipulasi villager
   - Beberapa server mungkin ban/restrict trade cycling

4. 💾 **Pilihan item tersimpan per profesi**
   - Tidak perlu setup ulang setiap kali
   - Pilihan berlaku untuk semua villager dengan profesi sama
   - Restart game tidak menghapus pilihan

## 🎯 Best Practices

1. **Setup trading hall** dengan compartment terpisah
2. **Label setiap villager** dengan name tag untuk tracking
3. **Keep backup villager** untuk profesi penting
4. **Document good trades** untuk referensi
5. **Be patient** - cycling butuh waktu tapi worth it!

---

**Happy Trading!** 🎉

Jika ada pertanyaan atau issue, silakan buka GitHub Issues atau contact developer.

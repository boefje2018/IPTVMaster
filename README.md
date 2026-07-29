# 📺 IPTV Master

> **Tamamen ücretsiz, açık kaynaklı IPTV izleme uygulaması** | **Fully free, open-source IPTV streaming application**

<div align="center">

![Android](https://img.shields.io/badge/Platform-Android-brightgreen?style=flat-square&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?style=flat-square&logo=kotlin)
![Min SDK](https://img.shields.io/badge/Min%20SDK-24-orange?style=flat-square)
![Target SDK](https://img.shields.io/badge/Target%20SDK-34-blue?style=flat-square)
![License](https://img.shields.io/badge/License-GPLv3-red?style=flat-square)
![Build](https://img.shields.io/github/actions/workflow/status/iptvmaster/iptvmaster/build.yml?style=flat-square)
![Release](https://img.shields.io/github/v/release/iptvmaster/iptvmaster?style=flat-square)

</div>

---

## 🇹🇷 Türkçe

**IPTV Master**, Android cihazlar için geliştirilmiş, modern ve kullanıcı dostu bir IPTV izleme uygulamasıdır. M3U ve M3U8 playlistlerinizi içe aktararak yüzlerce canlı TV kanalını, filmi ve diziyi tek bir uygulama üzerinden izleyebilirsiniz.

### Özellikler

- ✅ M3U / M3U8 playlist desteği
- ✅ HTTP, RTMP, HLS protokol desteği
- ✅ Gelişmiş ExoPlayer entegrasyonu
- ✅ Kanal gruplama ve filtreleme
- ✅ Favori kanallar
- ✅ EPG (Elektronik Program Rehberi) desteği
- ✅ Çoklu dil desteği (Türkçe, English)
- ✅ Materyal Tasarım (Material Design 3)
- ✅ Açık / Koyu tema
- ✅ SureStream (otomatik kalite seçimi)
- ✅ PIP (Picture-in-Picture) modu
- ✅ Chromecast desteği
- ✅ Kanal arama
- ✅ Son izlenenler listesi
- ✅ Dış oynatıcı desteği (VLC, MX Player vb.)
- ✅ Sıfır reklam - Tamamen ücretsiz

### Ekran Görüntüleri

| Ana Ekran | Kanal Listesi | Oynatıcı |
|-----------|--------------|----------|
| *ekran-goruntusu-1.png* | *ekran-goruntusu-2.png* | *ekran-goruntusu-3.png* |

### İndir

[![GitHub Release](https://img.shields.io/github/v/release/iptvmaster/iptvmaster?label=Son%20S%C3%BCr%C3%BCm&style=for-the-badge)](https://github.com/iptvmaster/iptvmaster/releases/latest)

En son APK'yı [GitHub Releases](https://github.com/iptvmaster/iptvmaster/releases) sayfasından indirebilirsiniz.

### Nasıl Kurulur?

1. APK dosyasını indirin
2. Android cihazınızda "Bilinmeyen kaynaklardan yükleme"yi etkinleştirin
3. APK'yı çalıştırın ve kurulumu tamamlayın
4. Uygulamayı açın ve M3U playlist URL'nizi girin

---

## 🇬🇧 English

**IPTV Master** is a modern and user-friendly IPTV streaming application developed for Android devices. Import your M3U and M3U8 playlists and watch hundreds of live TV channels, movies, and series from a single application.

### Features

- ✅ M3U / M3U8 playlist support
- ✅ HTTP, RTMP, HLS protocol support
- ✅ Advanced ExoPlayer integration
- ✅ Channel grouping and filtering
- ✅ Favorite channels
- ✅ EPG (Electronic Program Guide) support
- ✅ Multi-language support (English, Türkçe)
- ✅ Material Design 3
- ✅ Light / Dark theme
- ✅ SureStream (automatic quality selection)
- ✅ PIP (Picture-in-Picture) mode
- ✅ Chromecast support
- ✅ Channel search
- ✅ Recently watched list
- ✅ External player support (VLC, MX Player, etc.)
- ✅ Zero ads - Completely free

### Screenshots

| Home | Channel List | Player |
|------|-------------|--------|
| *screenshot-1.png* | *screenshot-2.png* | *screenshot-3.png* |

### Download

[![GitHub Release](https://img.shields.io/github/v/release/iptvmaster/iptvmaster?label=Latest%20Release&style=for-the-badge)](https://github.com/iptvmaster/iptvmaster/releases/latest)

Download the latest APK from [GitHub Releases](https://github.com/iptvmaster/iptvmaster/releases).

### How to Install?

1. Download the APK file
2. Enable "Install from unknown sources" on your Android device
3. Run the APK and complete the installation
4. Open the app and enter your M3U playlist URL

---

## 🛠 How to Build / Nasıl Derlenir

### Android Studio

```bash
# Clone the repository
git clone https://github.com/iptvmaster/iptvmaster.git

# Open with Android Studio
cd iptvmaster
# File -> Open -> select the project folder

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

### Command Line

```bash
# Make sure you have JDK 17 installed
git clone https://github.com/iptvmaster/iptvmaster.git
cd iptvmaster
chmod +x gradlew

# Build
./gradlew assembleRelease --no-daemon
```

### Requirements

- **JDK 17** or later
- **Android Studio** Hedgehog (2023.1.1) or later
- **Android SDK** 34
- **Min SDK** 24

---

## 🏗 Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin |
| UI Toolkit | Jetpack Compose + Material Design 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt / Dagger |
| Media Player | ExoPlayer |
| Networking | Retrofit + OkHttp |
| Image Loading | Coil |
| Video Protocol | M3U8 / HLS / RTMP |
| CI/CD | GitHub Actions |
| License | GPLv3 |

---

## ☕ Donate / Bağış

If you like this project, consider supporting us:

[![Buy Me a Coffee](https://img.shields.io/badge/Buy%20Me%20A%20Coffee-FFDD00?style=for-the-badge&logo=buy-me-a-coffee&logoColor=black)](https://www.buymeacoffee.com/iptvmaster)
[![PayPal](https://img.shields.io/badge/PayPal-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://paypal.me/iptvmaster)
[![GitHub Sponsors](https://img.shields.io/badge/GitHub%20Sponsors-EA4AAA?style=for-the-badge&logo=github-sponsors&logoColor=white)](https://github.com/sponsors/iptvmaster)

For more donation options, see [Donate.md](Donate.md).

---

## 📄 License

This project is licensed under the **GNU General Public License v3.0**. See the [LICENSE](LICENSE) file for details.

```
Copyright (C) 2024 IPTV Master

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
```

---

<div align="center">
  <sub>Made with ❤️ for the IPTV community</sub>
</div>

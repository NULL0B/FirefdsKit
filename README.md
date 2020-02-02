# Firefds Kit [Q]

Xposed module for Samsung Q devices.

XDA Discussion thread: https://forum.xda-developers.com/xposed/modules/xposed-firefds-kit-customization-t3908114

## Features
The module has the following features:
- Fake system status to Official
- Selectable advanced power menu options:
  - Power off
  - Restart
  - Emergency mode
  - Recovery
  - Download
  - Data mode switch
  - Screenshot
  - Switch User (when multi user is enabled)
  - SystemUI restart
  - Flashlight
  - Screen Recorder (requires Samsung screen recorder app installed)
- Disable restart confirmation
- Enable call recording
- Replace add call button instead of call recording
- Enable call recording from menu
- Auto call recording
- Hide VoLTE icon in status bar
- Hide persistent USB connection notification
- Hide persistent charging notification
- Enable block phrases in messages app settings
- Enable native blur on notification panel pull down
- Enable navigation bar color settings in Navigation Bar settings
- Enable multi user toggle
- Set max user value selector
- Show seconds in status bar clock toggle
- Show clock date on right of clock toggle
- Add date to status bar clock options
- Enable fingerprint unlock on reboot toggle
- Enable biometrics unlock on reboot toggle
- Add network speed menu to show network speed in the status bar
- Data icon symbol selection (4G, LTE, 4G+, 4.5G)
- Show Data usage view in quick panel
- Double tap for sleep
- Hide NFC icon
- Disable Bluetooth toggle popup
- Disable sync toggle popup
- Disable high level brightness poup
- Hide carrier label
- Carrier label size selection
- Disable loud volume warning
- Disable volume control sound
- Disable low battery sound
- Screen timeout settings
- NFC behavior settings
- Auto MTP
- Disable camera temperature check
- Enable camera shutter sound menu
- Disable call number formatting
- Disable SMS to MMS threshold
- Force MMS connect
- Bypass exchange security
- Disable signature check
- Disable secure flag

## Attention
This was built and tested on the Samsung Galaxy S8 G950F (Exynos) varient.
Confirmed working on:


EdXposed is still very much a WIP and is still in beta. 
**THERE COULD BE BUGS/CRASHES/BOOTLOOPS**, but it's pretty stable.
I've been working with both solohsu and C3C0 on testing EdXposed on Samsung devices.

## Installation

To install this module you need the following apps and modules installed on your device:
1. Magisk latest Canary build v20.4 and above - https://github.com/topjohnwu/magisk_files/tree/canary
2. Magisk Manager v7.5.1 and above - https://github.com/topjohnwu/Magisk/releases
3. Riru Magisk module v19.6 and above - https://github.com/RikkaApps/Riru/releases
4. EdXposed Magisk module v0.4.6.0_beta and above - https://github.com/ElderDrivers/EdXposed/releases
5. EdXposed Manager v4.5.4 and above - https://github.com/ElderDrivers/EdXposedManager/releases

## Known Issues

- Some features are removed on purpose. Since GravityBox has been working on Samsung Oreo devices without much issues, I only implemented features that need special Samsung coding. You can check [GravityBox for Q](https://forum.xda-developers.com/xposed/modules/app-gravitybox-v9-0-0-beta-1-android-9-t3908768) for additional features.

## External Libraries

The project uses the following libraries:
1. https://github.com/topjohnwu/libsu
2. https://github.com/rovo89/XposedBridge
3. https://github.com/rovo89/XposedMods/tree/master/XposedLibrary
5. Samsung framework libraries which are used for compile only

## Credits
This module wouldn't have been here without the following people:
- [RikkaW](https://github.com/RikkaApps) - Creator of Riru Magisk module, which provides a way to inject codes into zygote process
- [rovo89](https://github.com/rovo89) - Creator of the original Xposed framework APIs
- [solohsu](https://github.com/solohsu) and [MlgmXyysd](https://github.com/MlgmXyysd) - Creators of the EdXposed Magisk module and Installer that made all of this possible
- [C3C0](https://github.com/GravityBox) - Creator of GravityBox Xposed modules, which I learnt a lot from
- [Wanam](https://github.com/wanam) - Creator of the original XTouchWiz module, which this module is based on.
- [topjohnwu](https://github.com/topjohnwu) - Creator of Magisk
- [AbrahamGC](https://forum.xda-developers.com/member.php?u=7393522) - [For the Extended Power Menu - Pie - Odex framework Smali guide](https://forum.xda-developers.com/showpost.php?p=78910083&postcount=944)

This is a moded version of Wanam's XTouchWiz:
https://github.com/wanam/XTouchWiz

## Telegram
Announcements and pre release versions - https://t.me/firefdskit

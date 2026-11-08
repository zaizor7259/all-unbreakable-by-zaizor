# All Unbreakable by Zaizor

Fabric mod for Minecraft 26.2 / Java 25. In singleplayer and LAN, it can hide item durability bars, repair worn items, prevent durability loss, and keep anvils from wearing out. Dedicated server support is not available yet; the mod disables itself there.

## Install

Place the built JAR in the client `mods` folder with Fabric Loader and Fabric API. Mod Menu is optional; when present, it provides the on/off screen. In a dedicated server session, the switch is locked and explains that server support is unavailable. LAN worlds continue to work.

## Features and compatibility

The Mod Menu screen has one Enabled/Disabled switch. When enabled, it protects vanilla and modded item durability, hides durability bars, repairs worn items, and protects anvils. The setting is saved locally and follows Minecraft's selected language. Strings are packaged only under Minecraft 26.2 language-pack codes; uncommon constructed-language packs currently fall back to English.

When enabled in singleplayer or LAN, the mod restores damaged stacks in player inventories, equipped slots, and ender chests. Items already destroyed and removed from an inventory cannot be recreated.

## Build

Use JDK 25 and Gradle 9.9 or newer with the Fabric Loom version in `build.gradle`, then run `gradle build`. The output JAR is under `build/libs/`. Loom's current 26.2 setup uses the official client JAR on the compile classpath; it is not included in the mod JAR.

The durability hook covers mods that use Minecraft's normal `ItemStack.hurtAndBreak` method. A mod that directly edits damage components or implements a separate durability system may need an additional compatibility hook.

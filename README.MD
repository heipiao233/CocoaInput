# NeoCocoa
A fork of [CocoaInput (nakanotti version)](https://github.com/nakanotti/CocoaInput)
Support IME - Input Method Editor(Chinese,Japanese,Korean) in Minecraft on Multi-platforms(macOS, Windows, X11, Wayland).

## Compiling
1. Clone git repository
```Bash
git clone https://github.com/heipiao233/CocoaInput.git
cd CocoaInput
```
2. Compile native library<br />
CocoaInput needs native-platform libraries to work. 
To compile them, run one of below scripts which is your platform.
    - build_lib_for_mac.sh - For macOS
    - build_lib_for_x11.sh - For X11
    - build_lib_for_wayland.sh - For Wayland
    - build_lib_for_win.sh - For Windows
    - (build_lib_all.sh - For all platform, You should create remote_build.sh to build macOS lib remotely.)
3. Compile NeoForge Mod<br />
Type below command. NeoForge mod will be located in "forge/build/libs".
```Bash
./gradlew build
```
4. Compile Fabric Mod<br />
Type below command. Fabric mod will be located in "fabric/build/libs".
```Bash
cd fabric
./gradlew build
```

## Installing
Get jar by trying ”Compiling" task.
Place it in your mods directory.

CocoaInput requires NeoForge or Fabric.

This mod uses Java Native Access (Apache Licence2) and binuary for Minecraft 1.20.6 contains it.

## License
Minecraft Mod Public License

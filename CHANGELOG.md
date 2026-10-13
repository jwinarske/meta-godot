# Changelog

Oct 13, 2026
1. godot 4.7.2 (master: whinlatter/wrynose/blacksail, UNPACKDIR)
2. libgodot built from upstream godot 4.7.2 (library_type=shared_library); drop migeran fork
3. shared godot.inc for godot and libgodot
4. libgodot-sample: layer-owned host using upstream libgodot API + godot-cpp master
5. drop obsolete wayland-egl-core patch; refresh clang and AS/AR/RANLIB patches
6. honor PARALLEL_MAKE for scons num_jobs; accesskit disabled by default
7. pass LDFLAGS via scons linkflags (GNU_HASH QA)
8. use bundled harfbuzz by default; 4.7 needs harfbuzz-raster/-vector
9. drop zenity from godot RRECOMMENDS (pulls in webkitgtk)
10. graphite2: CMAKE_POLICY_VERSION_MINIMUM=3.5, include cstdint for tests
11. drop S/destsuffix=git overrides; use default ${BP}
12. libgodot-sample: RelWithDebInfo so godot-cpp doesn't strip at link
13. compile-tested for riscv64 (visionfive2, core-image-weston, master)

Aug 24, 2024
1. create dynamic-layer for clang-layer
2. remove ttf-noto recipe
3. update libgodot to 4.4 staging branch
4. remove godot-cpp-static recipe
5. update godot-cpp-template
6. update godot-demo-projects
7. update godot-tps-demo
8. update libgodot to install libgodot.h
9. update libgodot-sample
10. rename libgodot artifact to libgodot.so

Aug 19, 2024
1. godot 4.3

May 23, 2024
1. godot-master runs on raspberrypi5 core-image-weston with `godot --display-driver wayland --rendering-driver vulkan`
2. add runtime package dependencies

May 21, 2024
1. hard switch to clang for godot_4.2.2; will revisit later

May 20, 2024
1. libgodot and libgodot-native recipe
2. libgodot-sample application
3. godot-demo-projects
4. godot-tps-demo

May 19, 2024
1. godot-native support
2. godot-cpp-static recipe
3. godot-cpp-template recipe

May 18, 2024
1. Godot 4.2.2 building arm32, arm64, riscv64

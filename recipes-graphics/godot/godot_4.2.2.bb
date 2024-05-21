#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "GODOT Multi-platform 2D and 3D game engine"
DESCRIPTION = "Your free, open‑source game engine."
AUTHOR = "GODOT Engine authors"
HOMEPAGE = "https://github.com/godotengine/godot"
BUGTRACKER = "https://github.com/godotengine/godot/issues"
SECTION = "graphics"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=429628d6598a258acb0c2524e08a3036"

DEPENDS:class-native += " \
    libxkbcommon \
    python3-scons-native \
"

DEPENDS:class-target += " \
    compiler-rt \
    libcxx \
    libxkbcommon \
    python3-scons-native \
"

SRCREV = "15073afe3856abd2aa1622492fe50026c7d63dc1"
SRC_URI = " \
    git://github.com/godotengine/godot.git;protocol=https;lfs=0;nobranch=1 \
    file://0001-Add-AS-AR-RANLIB-RC.patch \
    file://0001-enable-clang.patch \
"

S = "${WORKDIR}/git"

BUILD_DIR="${S}/build"


RUNTIME:class-target = "llvm"
TOOLCHAIN:class-target = "clang"
PREFERRED_PROVIDER_libgcc:class-target = "compiler-rt"
LIBCPLUSPLUS:class-target = "-stdlib=libc++"

inherit pkgconfig

COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
COMPATIBLE_MACHINE:armv7 = "(.*)"
COMPATIBLE_MACHINE:armv7a = "(.*)"
COMPATIBLE_MACHINE:armv7ve = "(.*)"
COMPATIBLE_MACHINE:x86 = "(.*)"
COMPATIBLE_MACHINE:x86-64 = "(.*)"
COMPATIBLE_MACHINE:riscv64 = "(.*)"

TARGET_ARCH_NAME:aarch64 = "arm64"
TARGET_ARCH_NAME:armv7 = "arm32"
TARGET_ARCH_NAME:armv7a = "arm32"
TARGET_ARCH_NAME:armv7ve = "arm32"
TARGET_ARCH_NAME:x86 = "x86_32"
TARGET_ARCH_NAME:x86-64 = "x86_64"
TARGET_ARCH_NAME:riscv64 = "rv64"


PACKAGECONFIG:class-target ??= " \
    sowrap fontconfig dbus udev touch \
"

# remove x11 if wayland and x11 present.
PACKAGECONFIG:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland x11', 'x11', '', d)}"

# append features if present
PACKAGECONFIG:append = "${@bb.utils.filter('DISTRO_FEATURES', 'alsa opengl vulkan', d)}"

PACKAGECONFIG[opengl] = "opengl3=yes, opengl3=no, virtual/egl"
PACKAGECONFIG[vulkan] = "vulkan=yes use_volk=yes, vulkan=no use_volk=no, glslang vulkan-loader vulkan-volk"

PACKAGECONFIG[wayland] = "wayland=yes, wayland=no, wayland wayland-native"
PACKAGECONFIG[libdecor] = "libdecor=yes, libdecor=no, libdecor"
PACKAGECONFIG[x11] = "x11=yes, x11=no, libx11 libxcursor xinerama xext xrandr libxrender libxi"

PACKAGECONFIG[static_cpp] = "use_static_cpp=yes, use_static_cpp=no"
PACKAGECONFIG[lto] = "lto=auto, lto=none"
PACKAGECONFIG[sowrap] = "use_sowrap=yes, use_sowrap=no"
PACKAGECONFIG[debug] = "debug_symbols=yes, debug_symbols=no"

PACKAGECONFIG[alsa] = "alsa=yes, alsa=no, alsa-lib"
PACKAGECONFIG[pulseaudio] = "pulseaudio=yes, pulseaudio=no, pulseaudio"
PACKAGECONFIG[fontconfig] = "fontconfig=yes, fontconfig=no, fontconfig"
PACKAGECONFIG[dbus] = "dbus=yes, dbus=no, dbus"
PACKAGECONFIG[udev] = "udev=yes, udev=no, udev"
PACKAGECONFIG[touch] = "touch=yes, touch=no,"
PACKAGECONFIG[openxr] = "openxr=yes, openxr=no"

PACKAGECONFIG[sys_brotli] = "builtin_brotli=no, builtin_brotli=yes,brotli"
PACKAGECONFIG[sys_freetype] = "builtin_freetype=no, builtin_freetype=yes,freetype"
PACKAGECONFIG[sys_graphite] = "builtin_graphite=no, builtin_graphite=yes,graphite2"
PACKAGECONFIG[sys_harfbuzz] = "builtin_harfbuzz=no, builtin_harfbuzz=yes,harfbuzz"
PACKAGECONFIG[sys_icu4c] = "builtin_icu4c=no, builtin_icu4c=yes,icu"
PACKAGECONFIG[sys_libogg] = "builtin_libogg=no, builtin_libogg=yes,libogg"
PACKAGECONFIG[sys_libpng] = "builtin_libpng=no, builtin_libpng=yes,libpng"
PACKAGECONFIG[sys_libtheora] = "builtin_libtheora=no, builtin_libtheora=yes,libtheora"
PACKAGECONFIG[sys_libvorbis] = "builtin_libvorbis=no, builtin_libvorbis=yes,libvorbis"
PACKAGECONFIG[sys_libwebp] = "builtin_libwebp=no, builtin_libwebp=yes,libwebp"
PACKAGECONFIG[sys_zlib] = "builtin_zlib=no, builtin_zlib=yes,zlib"
PACKAGECONFIG[sys_zstd] = "builtin_zstd=no, builtin_zstd=yes,zstd"

do_compile:class-native () {

    cd ${S}
    scons p=linuxbsd target=editor production=yes no_editor_splash=yes
}

do_compile:class-target () {

    cd ${S}
    scons p=linuxbsd target=editor arch=${TARGET_ARCH_NAME} \
        use_llvm=yes lto=auto progress=no no_editor_splash=yes verbose=yes \
        ${PACKAGECONFIG_CONFARGS} \
        CC="${CC}" CFLAGS="${CFLAGS}" CXX="${CXX}" CXXFLAGS="${CXXFLAGS}" \
        AS="${AS}" AR="${AR}" RANLIB="${RANLIB}" LINK="${CXX} ${LDFLAGS} -fuse-ld=lld"
}

do_install:class-native () {

    install -d ${D}${bindir}
    install -m0755 ${S}/bin/godot.linuxbsd.editor.${TARGET_ARCH_NAME} ${D}${bindir}/godot
}

do_install:class-target () {

    install -d ${D}${bindir}
    install -m0755 ${S}/bin/godot.linuxbsd.editor.${TARGET_ARCH_NAME}.llvm ${D}${bindir}/godot
}

INSANE_SKIP:${PN} = "already-stripped"

FILES:${PN}-dev:class-native += "${datadir}"

RDEPENDS:${PN}:class-target += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'libx11 libxcursor xinerama xext xrandr libxrender libxi', '', d)} \
    fontconfig dbus udev \
"

RRECOMMENDS:${PN}:class-target += " \
    liberation-fonts \
"

BBCLASSEXTEND = "native nativesdk"

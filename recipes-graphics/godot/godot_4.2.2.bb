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

DEPENDS += " \
    libxkbcommon \
    python3-scons-native \
"

RDEPENDS:${PN} += "\
    ca-certificates \
"

SRCREV = "15073afe3856abd2aa1622492fe50026c7d63dc1"
SRC_URI = "git://github.com/godotengine/godot.git;protocol=https;lfs=0;nobranch=1"

S = "${WORKDIR}/git"

BUILD_DIR="${S}/build"

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


PACKAGECONFIG ??=	" \
    sowrap lto pulseaudio fontconfig dbus udev touch \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)} \
"
PACKAGECONFIG:aarch64:append ??= " \
    sys_brotli sys_freetype sys_graphite sys_harfbuzz sys_icu4c \
    sys_libogg sys_libpng sys_libtheora sys_libvorbis sys_libwebp \
    sys_zlib sys_zstd \
"
PACKAGECONFIG:x86_64:append ??= " \
    sys_brotli sys_freetype sys_graphite sys_harfbuzz sys_icu4c \
    sys_libogg sys_libpng sys_libtheora sys_libvorbis sys_libwebp \
    sys_zlib sys_zstd \
"
PACKAGECONFIG:riscv64:append ??= " \
    sys_brotli sys_freetype sys_graphite sys_harfbuzz sys_icu4c \
    sys_libogg sys_libpng sys_libtheora sys_libvorbis sys_libwebp \
    sys_zlib sys_zstd \
"


# remove x11 if wayland and x11 present.
PACKAGECONFIG:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland x11', 'x11', '', d)}"

# append features if present
PACKAGECONFIG:append = "${@bb.utils.filter('DISTRO_FEATURES', 'alsa opengl vulkan', d)}"

PACKAGECONFIG[opengl] = "opengl3=yes, opengl3=no, virtual/egl"
PACKAGECONFIG[vulkan] = "vulkan=yes, vulkan=no, glslang"
PACKAGECONFIG[volk] = "use_volk=yes, use_volk=no, vulkan-volk"

PACKAGECONFIG[wayland] = "wayland=yes, wayland=no, wayland wayland-native"
PACKAGECONFIG[libdecor] = "libdecor=yes, libdecor=no, libdecor"
PACKAGECONFIG[x11] = "x11=yes, x11=no, libx11 libxcursor xinerama xext xrandr libxrender libxi"

PACKAGECONFIG[llvm] = "use_llvm=yes, use_llvm=no,"
PACKAGECONFIG[static_cpp] = "use_static_cpp=yes, use_static_cpp=no"
PACKAGECONFIG[debug] = "debug_symbols=yes, debug_symbols=no"
PACKAGECONFIG[lto] = "lto=auto, lto=none"
PACKAGECONFIG[sowrap] = "use_sowrap=yes, use_sowrap=no"

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

PACKAGECONFIG[module_denoise] = "module_denoise_enabled=yes, module_denoise_enabled=no"
PACKAGECONFIG[module_raycast] = "module_raycast=yes, module_raycast=no"

do_compile () {

    cd ${S}
    scons p=linuxbsd target=editor arch=${TARGET_ARCH_NAME} \
        progress=no verbose=yes no_editor_splash=yes \
        ${PACKAGECONFIG_CONFARGS} \
        CC="${CC}" cflags="${CFLAGS}" \
        CXX="${CXX}" cxxflags="${CXXFLAGS}" \
        LINK="${LD}" linkflags="${LDFLAGS}" \
        import_env_vars="$(env)"
}

do_install () {

    install -d ${D}${bindir}
    install -m0755 ${S}/bin/godot.linuxbsd.editor.${TARGET_ARCH_NAME} ${D}${bindir}/godot
}

INSANE_SKIP:${PN} = "already-stripped"

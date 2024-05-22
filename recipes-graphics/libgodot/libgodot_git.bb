#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "GODOT Multi-platform 2D and 3D game engine"
DESCRIPTION = "Your free, open‑source game engine."
AUTHOR = "GODOT Engine authors"
HOMEPAGE = "https://github.com/migeran/libgodot_project"
BUGTRACKER = "https://github.com/migeran/libgodot_project/issues"
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

RDEPENDS:${PN} += "\
    ca-certificates \
"

SRCREV = "a82f487e7814219d4c4807bb147976dd8eefbf1c"
SRC_URI = " \
    git://github.com/migeran/godot.git;protocol=https;lfs=0;nobranch=1 \
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
    x11 \
    lto fontconfig dbus udev touch \
    \
    sys_brotli \
    sys_freetype \
    sys_graphite \
    sys_harfbuzz \
    sys_icu4c \
    sys_libogg \
    sys_libpng \
    sys_libtheora \
    sys_libvorbis \
    sys_libwebp \
    sys_zlib \
    sys_zstd \
"

# remove x11 if wayland and x11 are present.
PACKAGECONFIG:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland x11', 'x11', '', d)}"

# append features if present
PACKAGECONFIG:append = "${@bb.utils.filter('DISTRO_FEATURES', 'alsa opengl vulkan', d)}"

PACKAGECONFIG[opengl] = "opengl3=yes, opengl3=no, virtual/egl"
PACKAGECONFIG[vulkan] = "vulkan=yes use_volk=yes, vulkan=no use_volk=no, glslang vulkan-volk"

PACKAGECONFIG[wayland] = "wayland=yes, wayland=no, wayland wayland-native"
PACKAGECONFIG[libdecor] = "libdecor=yes, libdecor=no, libdecor"
PACKAGECONFIG[x11] = "x11=yes, x11=no, libx11 libxcursor xinerama xext xrandr libxrender libxi"

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

do_compile:class-native () {

    cd ${S}
    scons p=linuxbsd target=editor production=yes no_editor_splash=yes
}

do_compile:class-target () {

    cd ${S}
    scons p=linuxbsd target=editor arch=${TARGET_ARCH_NAME} \
        use_llvm=yes lto=auto progress=no no_editor_splash=yes verbose=yes \
        library_type=shared_library \
        ${PACKAGECONFIG_CONFARGS} \
        CC="${CC}" cflags="${CFLAGS}" \
        CXX="${CXX}" cxxflags="${CXXFLAGS}" \
        AS="${AS}" AR="${AR}" RANLIB="${RANLIB}" LINK="${CXX} ${LDFLAGS} -fuse-ld=lld" \
        import_env_vars=PATH,PKG_CONFIG_DIR,PKG_CONFIG_DISABLE_UNINSTALLED,PKG_CONFIG_LIBDIR,PKG_CONFIG_PATH,PKG_CONFIG_SYSROOT_DIR,PKG_CONFIG_SYSTEM_INCLUDE_PATH,PKG_CONFIG_SYSTEM_LIBRARY_PATH
}

do_install:class-native () {

    install -d ${D}${bindir}
    install -m0755 ${S}/bin/godot.linuxbsd.editor.${TARGET_ARCH_NAME} ${D}${bindir}/libgodot
}

do_install:class-target () {

    install -d ${D}${libdir}
    install -m0755 ${S}/bin/libgodot.linuxbsd.editor.${TARGET_ARCH_NAME}.llvm.so ${D}${libdir}/libgodot.so
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

INSANE_SKIP:${PN} = "already-stripped"

BBCLASSEXTEND = "native nativesdk"

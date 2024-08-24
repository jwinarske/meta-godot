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
    libxkbcommon \
    python3-scons-native \
"

RDEPENDS:${PN} += "\
    ca-certificates \
"

SRCREV = "0ff3c37043dd287b85d0fa4e5f425f21ef8147e3"
SRC_URI = " \
    git://github.com/migeran/godot.git;protocol=https;lfs=0;branch=libgodot_migeran_next \
    file://0001-Add-AS-AR-RANLIB-RC.patch \
    file://0001-enable-clang.patch \
"

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


PACKAGECONFIG:class-target ??= " \
    library \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)} \
    sowrap dbus fontconfig pulseaudio touch udev libdecor \
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

PACKAGECONFIG[sowrap] = "use_sowrap=yes, use_sowrap=no"
PACKAGECONFIG[debug] = "debug_symbols=yes, debug_symbols=no"

PACKAGECONFIG[llvm] = "use_llvm=yes lto=full LINK='${CXX} ${LDFLAGS} -fuse-ld=lld', \
                       use_llvm=no LINK='${CXX} ${LDFLAGS}'"

PACKAGECONFIG[library] = "library_type=shared_library"

PACKAGECONFIG[alsa] = "alsa=yes, alsa=no, alsa-lib"
PACKAGECONFIG[dbus] = "dbus=yes, dbus=no, dbus"
PACKAGECONFIG[fontconfig] = "fontconfig=yes, fontconfig=no, fontconfig"
PACKAGECONFIG[openxr] = "openxr=yes, openxr=no"
PACKAGECONFIG[pulseaudio] = "pulseaudio=yes, pulseaudio=no, pulseaudio"
PACAKGECONFIG[speechd] = "speechd=yes, speechd=no"
PACKAGECONFIG[touch] = "touch=yes, touch=no,"
PACKAGECONFIG[udev] = "udev=yes, udev=no, udev"

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
        use_static_cpp=yes \
        optimize=speed \
        progress=yes \
        no_editor_splash=yes \
        num_jobs=${BB_NUMBER_THREADS} \
        ${PACKAGECONFIG_CONFARGS} \
        CC="${CC}" cflags="${CFLAGS}" \
        CXX="${CXX}" cxxflags="${CXXFLAGS}" \
        AS="${AS}" AR="${AR}" RANLIB="${RANLIB}" \
        import_env_vars=PATH,PKG_CONFIG_DIR,PKG_CONFIG_DISABLE_UNINSTALLED,PKG_CONFIG_LIBDIR,PKG_CONFIG_PATH,PKG_CONFIG_SYSROOT_DIR,PKG_CONFIG_SYSTEM_INCLUDE_PATH,PKG_CONFIG_SYSTEM_LIBRARY_PATH
}

do_install:class-native () {

    install -d ${D}${bindir}
    install -m0755 ${S}/bin/godot.linuxbsd.editor.${TARGET_ARCH_NAME} ${D}${bindir}/libgodot

    install -d ${D}${includedir}
    install -m0644 ${S}/core/extension/libgodot.h ${D}${includedir}
}

do_install:class-target () {

    EDITOR="$(find ${S}/bin -iname libgodot.linuxbsd.editor*.so)"

    install -d ${D}${libdir}
    install -m0755 "${EDITOR}" "${D}${libdir}/libgodot.so"
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

INSANE_SKIP:${PN} = "already-stripped"

RDEPENDS:${PN}:class-target += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'libx11 libxcursor xinerama libxext xrandr libxrender libxi', '', d)} \
    ca-certificates fontconfig dbus udev \
    adwaita-icon-theme-cursors bash liberation-fonts pciutils xdg-user-dirs \
"

BBCLASSEXTEND = "native nativesdk"

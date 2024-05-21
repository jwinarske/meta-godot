#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "GDExtension example"
DESCRIPTION = "The Summator Example from Custom Modules made with the GDExtension system in Godot 4"
AUTHOR = "Patrick Exner"
HOMEPAGE = "https://github.com/paddy-exe/GDExtensionSummator"
BUGTRACKER = "https://github.com/paddy-exe/GDExtensionSummator/issues"
SECTION = "graphics"

LICENSE = "Unlicense"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=d88e9e08385d2a17052dac348bde4bc1"

DEPENDS += " \
    python3-scons-native \
"

SRC_URI = " \
    git://github.com/paddy-exe/GDExtensionSummator.git;protocol=https;lfs=0;branch=main;name=gdextension_summator \
    git://github.com/godotengine/godot-cpp.git;protocol=https;lfs=0;nobranch=1;name=godot_cpp;destsuffix=git/godot-cpp \
    file://CMakeLists.txt \
    file://export_presets.cfg \
"

SRCREV_FORMAT .= "_gdextension_summator"
SRCREV_gdextension_summator = "46a6b99b8e28fadb3764fbeb71d5669037046679"
SRCREV_FORMAT .= "_godot_cpp"
SRCREV_godot_cpp = "98c143a48365f3f3bf5f99d6289a2cb25e6472d1"


S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

TARGET_ARCH_NAME:aarch64 = "arm64"
TARGET_ARCH_NAME:armv7 = "arm32"
TARGET_ARCH_NAME:armv7a = "arm32"
TARGET_ARCH_NAME:armv7ve = "arm32"
TARGET_ARCH_NAME:x86 = "x86_32"
TARGET_ARCH_NAME:x86-64 = "x86_64"
TARGET_ARCH_NAME:riscv64 = "rv64"

inherit pkgconfig cmake

PACKAGECONFIG ??= " single"

PACKAGECONFIG[single] = "-DFLOAT_PRECISION=single, -DFLOAT_PRECISION=double"

EXTRA_OECMAKE += " \
    -D LIBRARY_OUTPUT_NAME=gdsummator.linux.template_debug.${TARGET_ARCH_NAME} \
"

do_configure:prepend() {
    cp ${WORKDIR}/CMakeLists.txt ${S}
}

do_install:append() {

    # copy and set architecture
    cp ${WORKDIR}/export_presets.cfg ${S}/game/export_presets.cfg
    sed -i "s/x86_64/${TARGET_ARCH_NAME}/g" ${S}/game/export_presets.cfg

    install -d ${D}${datadir}/godot/gdsummator
    cp -r ${S}/game/* ${D}${datadir}/godot/gdsummator/
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += " \
    ${libdir} \
    ${datadir}/godot/gdsummator \
"

#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "C++ bindings for the Godot script API"
DESCRIPTION = "This repository contains the C++ bindings for the Godot Engine's GDExtensions API."
AUTHOR = "GODOT Engine authors"
HOMEPAGE = "https://github.com/godotengine/godot-cpp"
BUGTRACKER = "https://github.com/godotengine/godot-cpp/issues"
SECTION = "graphics"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=de47f9dd55a8ac3f062f09491c90a979"

DEPENDS += " \
    godot-native \
"

SRCREV = "98c143a48365f3f3bf5f99d6289a2cb25e6472d1"
SRC_URI = "git://github.com/godotengine/godot-cpp.git;protocol=https;lfs=0;nobranch=1"

S = "${WORKDIR}/git"

inherit pkgconfig cmake

PACKAGECONFIG ??= " single"

PACKAGECONFIG[single] = "-DFLOAT_PRECISION=single, FLOAT_PRECISION=double"

EXTRA_OECMAKE += " \
    -D CMAKE_BUILD_TYPE=MinSizeRel \
    -D GODOT_GDEXTENSION_DIR=${WORKDIR}/generated \
    -D GODOT_CPP_SYSTEM_HEADERS=ON \
"

do_configure:prepend() {

    mkdir -p ${WORKDIR}/generated && cd ${WORKDIR}/generated

    # generate required files
    godot --headless --dump-extension-api --dump-gdextension-interface

    cd ${B}
}

do_install:append() {

    cd ${B}

    install -d ${D}${libdir}/godot
    cp -v ${B}/bin/libgodot-cpp.linux.*.a ${D}${libdir}/godot/

    install -d ${D}${includedir}/godot
    cp -rv ${S}/include/* ${D}${includedir}/godot/

    install -d ${D}${includedir}/godot/gen
    cp -v ${WORKDIR}/generated/* ${D}${includedir}/godot/gen/
}

FILES:${PN}-staticdev += " \
    ${includedir} \
    ${libdir} \
"

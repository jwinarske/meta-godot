#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "Migeran LibGodot Project"
DESCRIPTION = "LibGodot Sample application."
AUTHOR = "Migeran - Godot Engine & Robotics Experts"
HOMEPAGE = "https://github.com/migeran/libgodot_project"
BUGTRACKER = "https://github.com/migeran/libgodot_project/issues"
SECTION = "graphics"

LICENSE = "MIT"
LIC_FILES_CHKSUM = " \
    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

DEPENDS += " \
    libgodot-native \
"

RDEPENDS:${PN} += " \
    libgodot \
"

SRC_URI = " \
    git://github.com/migeran/libgodot_project.git;protocol=https;lfs=0;branch=main;name=libgodot \
    git://github.com/godotengine/godot-cpp.git;protocol=https;lfs=0;nobranch=1;name=godot_cpp;destsuffix=git/godot-cpp \
    file://CMakeLists.txt \
"

SRCREV_FORMAT .= "_libgodot"
SRCREV_libgodot = "26fc59a757d4b31691bbf15e88c909cc921158b3"
SRCREV_FORMAT .= "_godot_cpp"
SRCREV_godot_cpp = "98c143a48365f3f3bf5f99d6289a2cb25e6472d1"


S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit pkgconfig cmake

PACKAGECONFIG ??= " single"

PACKAGECONFIG[single] = "-DFLOAT_PRECISION=single, -DFLOAT_PRECISION=double"

EXTRA_OECMAKE += " \
    -D CMAKE_BUILD_TYPE=MinSizeRel \
    -D GODOT_GDEXTENSION_DIR=${WORKDIR}/generated \
    -D GODOT_CPP_SYSTEM_HEADERS=ON \
"

do_configure:prepend() {

    cp ${WORKDIR}/CMakeLists.txt ${S}
    rm ${S}/samples/cpp_sample/CMakeLists.txt | true

    mkdir -p ${WORKDIR}/generated && cd ${WORKDIR}/generated

    # generate required files
    libgodot --headless --dump-extension-api --dump-gdextension-interface

    cd ${B}
}

do_install:append() {
    install -d ${D}${datadir}/godot/libgodot-sample
    cp -r ${S}/samples/project/* ${D}${datadir}/godot/libgodot-sample/
    rm -rf ${D}${datadir}/godot/libgodot-sample/.git
}

FILES:${PN} += " \
    ${datadir}/godot/libgodot-sample \
"

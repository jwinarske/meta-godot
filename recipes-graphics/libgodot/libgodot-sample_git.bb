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
    git://github.com/migeran/libgodot_project.git;protocol=https;lfs=0;branch=libgodot_migeran_next;name=libgodot \
    git://github.com/godotengine/godot-cpp.git;protocol=https;lfs=0;nobranch=1;name=godot_cpp;destsuffix=git/godot-cpp \
    file://CMakeLists.txt \
"

SRCREV_FORMAT .= "_libgodot"
SRCREV_libgodot = "41b9a5781abea3f523b65557d118a2b4a02f968c"
SRCREV_FORMAT .= "_godot_cpp"
SRCREV_godot_cpp = "19c83a8837738e5014cc35771820bcb8cb73a5ea"


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

    # generate required files
    mkdir -p ${WORKDIR}/generated && cd ${WORKDIR}/generated
    libgodot --headless --dump-extension-api --dump-gdextension-interface

    cp ${STAGING_INCDIR_NATIVE}/libgodot.h .

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

#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "LibGodot sample host application"
DESCRIPTION = "Minimal host that embeds the upstream Godot engine via \
libgodot.so and runs a project; arguments are passed to the engine."
HOMEPAGE = "https://github.com/godotengine/godot"
SECTION = "graphics"

LICENSE = "MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

DEPENDS += " \
    godot-native \
    libgodot \
"

RDEPENDS:${PN} += " \
    libgodot \
"

RRECOMMENDS:${PN} += " \
    godot-demo-projects \
"

SRC_URI = " \
    git://github.com/godotengine/godot-cpp.git;protocol=https;lfs=0;branch=master;destsuffix=src/godot-cpp \
    file://CMakeLists.txt;subdir=src \
    file://main.cpp;subdir=src \
"

SRCREV = "b8530af7546a3fcae80cef2477526569fd42ffc2"

PV = "4.7.2+git"

S = "${UNPACKDIR}/src"

inherit pkgconfig cmake

PACKAGECONFIG ??= "single"

PACKAGECONFIG[single] = "-DGODOTCPP_PRECISION=single, -DGODOTCPP_PRECISION=double"

GODOT_GENERATED_DIR = "${WORKDIR}/generated"

# godot-cpp links with -s unless Debug/RelWithDebInfo; let OE do the stripping
EXTRA_OECMAKE += " \
    -D CMAKE_BUILD_TYPE=RelWithDebInfo \
    -D GODOTCPP_GDEXTENSION_DIR=${GODOT_GENERATED_DIR} \
    -D GODOTCPP_CUSTOM_API_FILE=${GODOT_GENERATED_DIR}/extension_api.json \
    -D GODOTCPP_SYSTEM_HEADERS=ON \
    -D LIBGODOT_INCLUDE_DIR=${STAGING_INCDIR}/godot \
"

do_configure:prepend() {
    # generate the GDExtension API and interface descriptions from the
    # matching engine release
    mkdir -p ${GODOT_GENERATED_DIR}
    (cd ${GODOT_GENERATED_DIR} && HOME=${WORKDIR} godot --headless \
        --dump-extension-api --dump-gdextension-interface-json)
}

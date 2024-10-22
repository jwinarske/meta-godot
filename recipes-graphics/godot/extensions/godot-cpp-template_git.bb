#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "quickstart template for GDExtension development with Godot 4.0+"
DESCRIPTION = "Quickstart template for GDExtension development with Godot"
AUTHOR = "GODOT Engine authors"
HOMEPAGE = "https://github.com/godotengine/godot-cpp-template"
BUGTRACKER = "https://github.com/godotengine/godot-cpp-template/issues"
SECTION = "graphics"

LICENSE = "Unlicense"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=d88e9e08385d2a17052dac348bde4bc1"

DEPENDS += " \
    godot-native \
    python3-scons-native \
"

SRC_URI = " \
    git://github.com/godotengine/godot-cpp-template.git;protocol=https;lfs=0;branch=main;name=godot_cpp_template \
    git://github.com/godotengine/godot-cpp.git;protocol=https;lfs=0;branch=master;name=godot_cpp;destsuffix=git/godot-cpp \
    file://CMakeLists.txt \
"

SRCREV_FORMAT .= "_godot_cpp_template"
SRCREV_godot_cpp_template = "0536d636fa2aa8e2457f5013a85817537a6f3808"
SRCREV_FORMAT .= "_godot_cpp"
SRCREV_godot_cpp = "fbbf9ec4efd8f1055d00edb8d926eef8ba4c2cce"


S = "${WORKDIR}/git"

inherit pkgconfig cmake

PACKAGECONFIG ??= " single"

PACKAGECONFIG[single] = "-DFLOAT_PRECISION=single, -DFLOAT_PRECISION=double"

do_configure:prepend() {
    mv ${WORKDIR}/CMakeLists.txt ${S}
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} = "${libdir}"

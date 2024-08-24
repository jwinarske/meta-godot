#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "Godot Third Person Shooter with high quality assets and lighting"
DESCRIPTION = "Third person shooter demo made using Godot Engine"
AUTHOR = "Godot Engine contributors"
HOMEPAGE = "https://github.com/godotengine/tps-demo"
BUGTRACKER = "https://github.com/godotengine/tps-demo/issues"
SECTION = "graphics"

LICENSE = "CC-BY-3.0&MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=4f0aa3805663712e6417e4f6486cb791"

RDEPENDS:${PN} += " \
    godot \
"

SRC_URI = " \
    git://github.com/godotengine/tps-demo.git;protocol=https;lfs=0;branch=master \
"

SRCREV = "690ee91837aa64d74ba73bc925ec52886c879974"

S = "${WORKDIR}/git"

do_install() {

    install -d ${D}${datadir}/godot/tps
    cp -r ${S}/* ${D}${datadir}/godot/tps
    rm -rf ${D}${datadir}/godot/tps/.git
}

FILES:${PN} = "${datadir}/godot/tps"

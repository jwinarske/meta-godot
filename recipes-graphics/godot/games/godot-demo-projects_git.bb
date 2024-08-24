#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "Demonstration and Template Projects"
DESCRIPTION = "Demonstration and Template Projects"
AUTHOR = "Godot contributors"
HOMEPAGE = "https://github.com/godotengine/godot-demo-projects"
BUGTRACKER = "https://github.com/godotengine/godot-demo-projects/issues"
SECTION = "graphics"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=ad1317f093b82a7268bcdcbf75782b20"

RDEPENDS:${PN} += " \
    godot \
"

SRC_URI = " \
    git://github.com/godotengine/godot-demo-projects.git;protocol=https;lfs=0;branch=master \
"

SRCREV = "31d1c0c1122717b3ad74aeb94255bc38def52747"

S = "${WORKDIR}/git"

do_install() {

    install -d ${D}${datadir}/godot/godot-demo-projects
    cp -r ${S}/* ${D}${datadir}/godot/godot-demo-projects
    rm -rf ${D}${datadir}/godot/godot-demo-projects/.git
}

FILES:${PN} = "${datadir}/godot/godot-demo-projects"

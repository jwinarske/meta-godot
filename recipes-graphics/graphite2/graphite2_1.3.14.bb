#
# Copyright (c) 2020-2024 Joel Winarske. All rights reserved.
#

SUMMARY = "Font rendering capabilities for complex non-Roman writing systems"
HOMEPAGE = "http://sourceforge.net/projects/silgraphite"

LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b0452d508cc4eb104de0226a5b0c8786"

DEPENDS += "freetype"

SRCREV = "92f59dcc52f73ce747f1cdc831579ed2546884aa"
SRC_URI = "git://github.com/silnrsi/graphite.git;protocol=https;branch=master"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE += "-DLIB_SUFFIX=${@d.getVar('baselib').replace('lib', '')}"

BBCLASSEXTEND = "native"

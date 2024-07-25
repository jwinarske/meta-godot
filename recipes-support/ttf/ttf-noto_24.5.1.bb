# SPDX-FileCopyrightText: 2024 Joel Winarske <joel.winarske@gmail.com>
#
# SPDX-License-Identifier: MIT

DESCRIPTION = "Google Noto fonts"
HOMEPAGE = "https://www.google.com/get/noto/"
SECTION = "fonts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI = "https://github.com/notofonts/notofonts.github.io/archive/refs/tags/noto-monthly-release-24.5.1.tar.gz"
SRC_URI[md5sum] = "809844608ef28a17c661952b7ec8f1c3"
SRC_URI[sha256sum] = "431ef733310362b54732c7abc66b23e4c6751c3f8e6134efc64a4eed81188af6"

S = "${WORKDIR}/notofonts.github.io-noto-monthly-release-24.5.1"

inherit allarch fontcache

INHIBIT_DEFAULT_DEPS = "1"

PACKAGES = "ttf-noto"
FONT_PACKAGES = "ttf-noto"

FILES:${PN}  = " ${datadir}/fonts/truetype/ "

do_install() {
    install -d ${D}${datadir}/fonts/truetype/
    find ./hinted/ -name '*.tt[cf]' -exec install -m 0644 {} ${D}${datadir}/fonts/truetype/ \;
    find ./unhinted/ -name '*.tt[cf]' -exec install -m 0644 {} ${D}${datadir}/fonts/truetype/ \;
}

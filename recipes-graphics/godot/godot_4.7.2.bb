#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "GODOT Multi-platform 2D and 3D game engine"

require godot.inc

do_install:class-native () {

    install -d ${D}${bindir}
    install -m0755 ${S}/bin/godot.linuxbsd.editor.${TARGET_ARCH_NAME} ${D}${bindir}/godot
}

do_install:class-target () {

    EDITOR="$(find ${S}/bin -iname 'godot.linuxbsd.${GODOT_TARGET}*')"

    install -d ${D}${bindir}
    install -m0755 "${EDITOR}" "${D}${bindir}/godot"
}

FILES:${PN}-dev:class-native += "${datadir}"

RDEPENDS:${PN}:class-target += " \
    bash \
    pciutils \
    xdg-user-dirs \
"

RRECOMMENDS:${PN}:class-target += "\
    adwaita-icon-theme-cursors \
    liberation-fonts \
"

BBCLASSEXTEND = "native nativesdk"

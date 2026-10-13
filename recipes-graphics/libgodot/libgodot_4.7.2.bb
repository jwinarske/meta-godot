#
# Copyright (c) 2024 Joel Winarske. All rights reserved.
#

SUMMARY = "LibGodot - Godot engine as an embeddable shared library"
DESCRIPTION = "LibGodot builds the Godot engine as a shared library that a \
host application can load and drive via libgodot_create_godot_instance()."

require recipes-graphics/godot/godot.inc

GODOT_LIBRARY_TYPE = "shared_library"

PACKAGECONFIG:append:class-target = " sowrap"

do_install () {

    LIBGODOT="$(find ${S}/bin -iname 'libgodot.linuxbsd.${GODOT_TARGET}*.so')"

    install -d ${D}${libdir}
    install -m0755 "${LIBGODOT}" "${D}${libdir}/libgodot.so"

    # libgodot.h includes "core/extension/gdextension_interface.gen.h";
    # consumers add -I${includedir}/godot
    install -d ${D}${includedir}/godot/core/extension
    install -m0644 ${S}/core/extension/libgodot.h \
        ${S}/core/extension/gdextension_interface.gen.h \
        ${D}${includedir}/godot/core/extension/
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

RDEPENDS:${PN}:class-target += " \
    adwaita-icon-theme-cursors \
    liberation-fonts \
    pciutils \
    xdg-user-dirs \
"

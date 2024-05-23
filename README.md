# meta-godot

Yocto layer for GODOT related recipes

### RaspberryPi5 with Vulkan enabled core-image-weston image

conf/local.conf append
```
INHERIT += "rm_work"

INIT_MANAGER = "systemd"

# localization
DEFAULT_TIMEZONE = "America/Los_Angeles"
IMAGE_INSTALL:append = " \
    tzdata-core \
    tzdata-americas \
"

DISTRO_FEATURES:append = " systemd alsa wifi bluetooth usbhost pipewire"
DISTRO_FEATURES:remove = "sysvinit usbgadget ptest xen x11"

COMBINED_FEATURES += "alsa "
INIT_MANAGER = "systemd"

DISTRO_FEATURES:append = " opengl wayland pam"
PACKAGECONFIG:append:pn-weston = " remoting"

DISTRO_FEATURES:append = " vulkan"
IMAGE_INSTALL:append = " \
    vulkan-tools \
"

DISTRO_FEATURES:append = " acl xattr pam selinux audit"
PREFERRED_PROVIDER_virtual/refpolicy = "refpolicy-mls"

DISTRO_FEATURES:append = " security seccomp"

DISTRO_FEATURES:append = " virtualization kvm"

IMAGE_INSTALL:append = " \
    kvmtool dmidecode \
    libvirt libvirt-libvirtd libvirt-virsh \
"

LINUX_KERNEL_TYPE = "preempt-rt"
CMDLINE_DEBUG = "quiet"
DISPMANX_OFFLINE = "1"
DISABLE_OVERSCAN = "1"
DISABLE_RPI_BOOT_LOGO = "1"
DISABLE_SPLASH = "1"
IMAGE_FEATURES:remove = "splash"

VIDEO_CAMERA = "1"
RASPBERRYPI_CAMERA_V3 = "1"
ENABLE_I2C = "1"
KERNEL_MODULE_AUTOLOAD:rpi:append = " i2c-dev i2c-bcm2708"

PREFERRED_PROVIDER_jpeg = "libjpeg-turbo"
PREFERRED_PROVIDER_jpeg-native = "libjpeg-turbo-native"

LICENSE_FLAGS_ACCEPTED += " synaptics-killswitch"
```

Running
```
godot --display-driver wayland --rendering-driver vulkan
```

### Layer Dependencies

```
meta-clang
```
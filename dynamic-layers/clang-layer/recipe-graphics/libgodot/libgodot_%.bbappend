
DEPENDS:class-target += " \
    compiler-rt \
    libcxx \
"

RUNTIME:class-native = "llvm"
TOOLCHAIN:class-native = "clang"
PREFERRED_PROVIDER_libgcc:class-native = "compiler-rt"
LIBCPLUSPLUS:class-native = "-stdlib=libc++"

PACKAGECONFIG:append = "llvm"

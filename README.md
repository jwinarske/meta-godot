# meta-godot

Yocto/OpenEmbedded layer for the [Godot](https://godotengine.org) engine.

## Recipes

| Recipe | Description |
|---|---|
| `godot` | Godot 4.7.2 editor (target and native) |
| `libgodot` | Godot 4.7.2 as a shared library (`libgodot.so`) |
| `libgodot-sample` | minimal host app embedding `libgodot` |
| `godot-demo-projects`, `godot-tps-demo` | demo projects |
| `godot-cpp-template`, `gdextension-summator` | GDExtension examples |

## Compatibility

`master` branch: whinlatter, wrynose, blacksail. Use the `scarthgap` branch for scarthgap.

## Dependencies

- `openembedded-core`

Optional: `meta-clang` (clang build of `godot-native`), `meta-gnome`.

## Configuration

`PACKAGECONFIG` follows `DISTRO_FEATURES` (`wayland`, `x11`, `opengl`, `vulkan`, `alsa`). Godot's bundled harfbuzz is used by default; `sys_harfbuzz` needs harfbuzz built with `raster` and `vector`.

## Running

```
godot.linuxbsd.editor.<arch> --display-driver wayland --rendering-driver vulkan
```

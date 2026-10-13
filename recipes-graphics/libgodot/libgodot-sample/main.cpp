/*
 * Copyright (c) 2026 Joel Winarske. All rights reserved.
 * SPDX-License-Identifier: MIT
 *
 * Minimal LibGodot host: embeds the upstream Godot engine (libgodot.so)
 * and runs a project. All command line arguments are passed through to
 * the engine, e.g.:
 *
 *   libgodot-sample --path /usr/share/godot/demo-projects/2d/platformer
 */

#include <cstdio>
#include <cstdlib>

#include <libgodot.h>

#include <godot_cpp/classes/godot_instance.hpp>
#include <godot_cpp/core/defs.hpp>
#include <godot_cpp/godot.hpp>

extern "C" {

static void initialize_host_module(godot::ModuleInitializationLevel p_level) {}

static void uninitialize_host_module(godot::ModuleInitializationLevel p_level) {}

GDExtensionBool GDE_EXPORT libgodot_sample_init(GDExtensionInterfaceGetProcAddress p_get_proc_address,
		GDExtensionClassLibraryPtr p_library,
		GDExtensionInitialization *r_initialization) {
	godot::GDExtensionBinding::InitObject init_object(p_get_proc_address, p_library, r_initialization);

	init_object.register_initializer(initialize_host_module);
	init_object.register_terminator(uninitialize_host_module);
	init_object.set_minimum_library_initialization_level(godot::MODULE_INITIALIZATION_LEVEL_SCENE);

	return init_object.init();
}
}

int main(int argc, char **argv) {
	GDExtensionObjectPtr handle = libgodot_create_godot_instance(argc, argv, libgodot_sample_init);
	if (handle == nullptr) {
		fprintf(stderr, "Error creating Godot instance\n");
		return EXIT_FAILURE;
	}

	auto *instance = reinterpret_cast<godot::GodotInstance *>(godot::internal::get_object_instance_binding(handle));

	instance->start();
	while (!instance->iteration()) {
	}

	libgodot_destroy_godot_instance(handle);

	return EXIT_SUCCESS;
}

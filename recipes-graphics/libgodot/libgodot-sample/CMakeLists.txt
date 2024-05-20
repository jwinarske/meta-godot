cmake_minimum_required(VERSION 3.10.2)

project(libgodot-sample VERSION 0.1.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_EXTENSIONS OFF)

set(CMAKE_POSITION_INDEPENDENT_CODE ON)

add_subdirectory(godot-cpp)

add_executable(${PROJECT_NAME} samples/cpp_sample/src/main.cpp)

target_link_libraries(${PROJECT_NAME} PRIVATE godot::cpp)

# Toolchain IPO/LTO support
cmake_policy(SET CMP0069 NEW)
include(CheckIPOSupported)
check_ipo_supported(RESULT IPO_SUPPORT_RESULT OUTPUT IPO_SUPPORT_OUTPUT LANGUAGES CXX)
if (IPO_SUPPORT_RESULT)
    set_property(TARGET godot-cpp       PROPERTY INTERPROCEDURAL_OPTIMIZATION TRUE)
    set_property(TARGET ${PROJECT_NAME} PROPERTY INTERPROCEDURAL_OPTIMIZATION TRUE)
endif ()

install(TARGETS ${PROJECT_NAME} DESTINATION bin)

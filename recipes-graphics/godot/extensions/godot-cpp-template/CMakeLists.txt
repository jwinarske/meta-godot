cmake_minimum_required(VERSION 3.10.2)

if (NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE "MinSizeRelease" CACHE STRING "Choose the type of build, options are: Debug, Release, RelWithDebInfo, or MinSizeRel." FORCE)
    message(STATUS "CMAKE_BUILD_TYPE not set, defaulting to MinSizeRelease.")
endif ()

project(godot-cpp-template LANGUAGES CXX)

set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_POSITION_INDEPENDENT_CODE ON)

add_subdirectory(godot-cpp)

add_library(${PROJECT_NAME} SHARED src/register_types.cpp)
target_include_directories(${PROJECT_NAME} PRIVATE src)
target_link_libraries(${PROJECT_NAME} PRIVATE godot::cpp)

# Toolchain IPO/LTO support
cmake_policy(SET CMP0069 NEW)
include(CheckIPOSupported)
check_ipo_supported(RESULT IPO_SUPPORT_RESULT OUTPUT IPO_SUPPORT_OUTPUT LANGUAGES CXX)
if (IPO_SUPPORT_RESULT)
    set_property(TARGET godot-cpp       PROPERTY INTERPROCEDURAL_OPTIMIZATION TRUE)
    set_property(TARGET ${PROJECT_NAME} PROPERTY INTERPROCEDURAL_OPTIMIZATION TRUE)
endif ()

install(TARGETS ${PROJECT_NAME} DESTINATION lib/godot)

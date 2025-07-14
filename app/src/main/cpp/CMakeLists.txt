# 指定 CMake 的最低版本要求
# 请根据你实际安装的 CMake 版本进行调整，通常在 3.x.x 范围。
# 可以在 Android Studio SDK Manager 中查看，或在终端运行 `cmake --version`。
cmake_minimum_required(VERSION 3.22.1) # 替换为你实际的稳定版本

# 定义你的项目名称
project("your-native-project")

# 添加你的原生库
# 第一个参数是库的名称，它将决定生成的 .so 文件名 (lib<your_library_name>.so)
# 也是 System.loadLibrary() 和 target_link_libraries() 中使用的名称。
# 第三个参数是你的 C/C++ 源文件列表。
#add_library(
        #depth_estimation_jni # <--- 你的库名称 (与 target_link_libraries 保持一致)
        #SHARED               # <--- 这是一个共享库
        #native-lib.cpp       # <--- 你的 C++ 源文件。请确保这个文件存在于 src/main/cpp 目录下
        # another_file.cpp   # 如果有其他源文件，可以在这里添加
#)

# 查找 Android NDK 提供的 log 库（可选，如果你需要在 C++ 中使用 Android Log）
find_library( # Sets the name of the path variable.
        log-lib
        log
)

# 查找 C++ 标准库
find_library(
        cxx_shared_lib
        c++_shared
)

# 将你的原生库与所需的库链接
target_link_libraries(
        #depth_estimation_jni # <--- 你的库名称
       #${log-lib}           # <--- 链接 Android Log 库
        ${cxx_shared_lib}    # <--- 链接 C++ 共享标准库 (修复 libc++_shared.so not found 错误)
        # ... 如果你的库还依赖其他 .so 文件，在这里添加，例如 -landroid
)
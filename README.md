# cvdroid

<br>

### 1. 相关 Makefile 简介

*  Application.mk

```
# 使用 GNU libstdc++ 作为静态库，因为 STLport 会导致后面 OpenCV 静态库编译链接时出错
# APP_STL := stlport_static
APP_STL := gnustl_static

# 指定需要编译的CPU架构
# APP_ABI := all
APP_ABI := armeabi-v7a arm64-v8a
```

* Android.mk

```
# 指定 OPENCV_LIB_TYPE 为 STATIC，使 OpenCV.mk 中选用静态库来编译
# 为什么要选用静态库呢？请见下文分解。
OPENCV_LIB_TYPE:=STATIC
include jni/sdk/native/jni/OpenCV.mk
```

* OpenCV.mk

为什么要选用静态库呢？从 OpenCV.mk 中我们可以看到，官方已经给我们提供了一个集大成的 .so 动态库，叫做 libopencv_java3.so；另外还提供了各个功能模块的 .a 静态库。OpenCV 功能模块比较多，集大成的 .so 库自然也比较胖，我们希望能苗条一点，因此选用两个真正用到的 .a 库(imgproc 和 core)即可。

```
ifeq ($(OPENCV_LIB_TYPE),SHARED)
    OPENCV_LIBS_DIR:=$(OPENCV_THIS_DIR)/../libs/$(OPENCV_TARGET_ARCH_ABI)
    OPENCV_LIB_SUFFIX:=so
else
    OPENCV_LIBS_DIR:=$(OPENCV_THIS_DIR)/../staticlibs/$(OPENCV_TARGET_ARCH_ABI)
    OPENCV_LIB_SUFFIX:=a
    OPENCV_INSTALL_MODULES:=on
endif
```

好了，前面已经设置 OPENCV_LIB_TYPE 为 STATIC，接下来看看具体模块的 include 过程：

```
# 指定需要编译的模块 ☜ ☜ ☜ 裁剪就是这里啦！！
# OPENCV_MODULES:=shape ml dnn objdetect superres stitching videostab calib3d features2d highgui videoio imgcodecs video photo imgproc flann core
OPENCV_MODULES:=imgproc core

# 赋值 OPENCV_LIBS 变量
ifeq ($(OPENCV_LIB_TYPE),SHARED)
    OPENCV_LIBS:=java3
    OPENCV_LIB_TYPE:=SHARED
else
    OPENCV_LIBS:=$(OPENCV_MODULES)
    OPENCV_LIB_TYPE:=STATIC
endif

# For 循环拿到 OPENCV_LIBS 里前面所指定的模块名，执行宏定义 add_opencv_module
ifeq ($(OPENCV_MK_$(OPENCV_TARGET_ARCH_ABI)_ALREADY_INCLUDED),)
    ifeq ($(OPENCV_INSTALL_MODULES),on)
        $(foreach module,$(OPENCV_LIBS),$(eval $(call add_opencv_module,$(module))))
    endif
    ...
endif

# include 具体的模块，后续进行编译
define add_opencv_module
    include $(CLEAR_VARS)
    LOCAL_MODULE:=opencv_$1
    LOCAL_SRC_FILES:=$(OPENCV_LIBS_DIR)/libopencv_$1.$(OPENCV_LIB_SUFFIX)
    include $(PREBUILT_$(OPENCV_LIB_TYPE)_LIBRARY)
endef
```

<br>

### 2. 扫描效果优化

* 保证拍摄的图片清晰，避免变形操作

* 无须额外做腐蚀和模糊处理，灰度后直接做自适应阈值化

```
    cvtColor(mbgra, dst, CV_BGR2GRAY);
    adaptiveThreshold(dst, dst, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 25, 13);
```

* 后续可以适当加些锐化和明度增强之类的处理，可能效果会更好

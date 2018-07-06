LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=STATIC
include jni/sdk/native/jni/OpenCV.mk

LOCAL_MODULE    := CVDroid
LOCAL_SRC_FILES := cvdroid.cpp
LOCAL_LDLIBS    += -lm -llog -landroid
LOCAL_LDFLAGS += -ljnigraphics

include $(BUILD_SHARED_LIBRARY)
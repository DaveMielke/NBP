LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := nbp_common
LOCAL_SRC_FILES := files.c
LOCAL_CFLAGS := -Wall
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)


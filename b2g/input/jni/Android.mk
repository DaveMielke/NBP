LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := InputService
LOCAL_SRC_FILES := utils.c uinput.c
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)


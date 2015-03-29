LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := UserInterface
LOCAL_SRC_FILES := utils.c uinput.c kbddev.c kbdmon.c braille.c
LOCAL_CFLAGS := -Wall
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)


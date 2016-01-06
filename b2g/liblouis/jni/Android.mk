LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := louis

LOCAL_C_INCLUDES := $(LOCAL_PATH) liblouis-c/liblouis
LOCAL_CFLAGS := -Wall -DTABLESDIR=\"liblouis-c/tables\"
LOCAL_LDLIBS := -llog

LOCAL_SRC_FILES := \
   liblouis-c/liblouis/compileTranslationTable.c \
   liblouis-c/liblouis/lou_translateString.c \
   liblouis-c/liblouis/lou_backTranslateString.c \
   liblouis-c/liblouis/logging.c \
   liblouis-c/liblouis/wrappers.c \
   log.c \
   translation.c \
   louis.c

include $(BUILD_SHARED_LIBRARY)

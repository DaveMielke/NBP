LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := louis

LOCAL_C_INCLUDES := $(LOCAL_PATH) src/liblouis
LOCAL_CFLAGS := -Wall -DTABLESDIR=\"src/tables\"
LOCAL_LDLIBS := -llog

LOCAL_SRC_FILES := \
   src/liblouis/compileTranslationTable.c \
   src/liblouis/lou_translateString.c \
   src/liblouis/lou_backTranslateString.c \
   src/liblouis/logging.c \
   src/liblouis/wrappers.c

include $(BUILD_SHARED_LIBRARY)

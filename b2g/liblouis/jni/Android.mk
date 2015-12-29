LOCAL_PATH := $(call my-dir)
SOURCE_PATH := ../git

include $(CLEAR_VARS)
LOCAL_MODULE := louis

LOCAL_C_INCLUDES := $(LOCAL_PATH) $(SOURCE_PATH)
LOCAL_CFLAGS := -Wall -DTABLESDIR=\"$(SOURCE_PATH)/tables\"
LOCAL_LDLIBS := -llog

LOCAL_SRC_FILES := \
   $(SOURCE_PATH)/liblouis/compileTranslationTable.c \
   $(SOURCE_PATH)/liblouis/lou_translateString.c \
   $(SOURCE_PATH)/liblouis/lou_backTranslateString.c \
   $(SOURCE_PATH)/liblouis/logging.c \
   $(SOURCE_PATH)/liblouis/wrappers.c

include $(BUILD_SHARED_LIBRARY)

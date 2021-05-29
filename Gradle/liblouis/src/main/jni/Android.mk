LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := louis

LOCAL_C_INCLUDES := $(LOCAL_PATH) $(LOCAL_PATH)/liblouis-repository/liblouis
LOCAL_CFLAGS := -std=c99 -Wall -DTABLESDIR=\"liblouis-tables\"
LOCAL_LDLIBS := -llog

LOCAL_SRC_FILES := \
   liblouis-repository/liblouis/compileTranslationTable.c \
   liblouis-repository/liblouis/commonTranslationFunctions.c \
   liblouis-repository/liblouis/lou_translateString.c \
   liblouis-repository/liblouis/lou_backTranslateString.c \
   liblouis-repository/liblouis/pattern.c \
   liblouis-repository/liblouis/metadata.c \
   liblouis-repository/liblouis/logging.c \
   liblouis-repository/liblouis/utils.c \
   log.c \
   translator.c \
   table.c \
   metadata.c \
   emphasis.c \
   louis.c

include $(BUILD_SHARED_LIBRARY)

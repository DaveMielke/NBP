all: apk

ANDROID_NATIVE_DIRECTORY = jni
ANDROID_ASSETS_DIRECTORY = assets
ANDROID_BINARIES_DIRECTORY = bin
ANDROID_LIBRARIES_DIRECTORY = libs

ANDROID_PLATFORM_DIRECTORY = $(ANDROID_LIBRARIES_DIRECTORY)/$(PLATFORM_NAME)

ANDROID_LOCAL_PROPERTIES = local.properties
$(ANDROID_LOCAL_PROPERTIES): FORCE
	android update project --path .

NATIVE_SOURCES := Android.mk
LOCAL_FILES := $(ANDROID_LOCAL_PROPERTIES)


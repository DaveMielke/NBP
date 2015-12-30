include ../../prologue.mk

APPLICATION_NAME = b2g_ui
PACKAGE_PATH = org.nbp.b2g.ui
PLATFORM_NAME = armeabi

NATIVE_SOURCES += utils.h
NATIVE_SOURCES += utils.c
NATIVE_SOURCES += fwver.c
NATIVE_SOURCES += uinput.c
NATIVE_SOURCES += keyboard.c
NATIVE_SOURCES += kbdmon.c
NATIVE_SOURCES += metec_flat20_ioctl.h
NATIVE_SOURCES += brldev.c

ANDROID_BUILD_REVISION = $(ANDROID_ASSETS_DIRECTORY)/build.revision
LOCAL_FILES += $(ANDROID_BUILD_REVISION)
$(ANDROID_BUILD_REVISION): FORCE
	git describe --always >$@

ANDROID_BUILD_TIME = $(ANDROID_ASSETS_DIRECTORY)/build.time
LOCAL_FILES += $(ANDROID_BUILD_TIME)
$(ANDROID_BUILD_TIME): FORCE
	date --utc +'%Y-%m-%d@%H:%M %Z' >$@

ANDROID_FILES += custom_rules.xml
ANDROID_FILES += signing.properties

include ../../epilogue.mk

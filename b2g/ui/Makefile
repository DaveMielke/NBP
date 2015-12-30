include ../../prologue.mk

APPLICATION_NAME = b2g_ui
PACKAGE_PATH = org.nbp.b2g.ui
PLATFORM_NAME = armeabi

JNI_SOURCES += utils.h
JNI_SOURCES += utils.c
JNI_SOURCES += fwver.c
JNI_SOURCES += uinput.c
JNI_SOURCES += keyboard.c
JNI_SOURCES += kbdmon.c
JNI_SOURCES += metec_flat20_ioctl.h
JNI_SOURCES += brldev.c

ANDROID_BUILD_REVISION = $(ANDROID_ASSETS_DIRECTORY)/build.revision
ANDROID_LOCAL_FILES += $(ANDROID_BUILD_REVISION)
$(ANDROID_BUILD_REVISION): FORCE
	git describe --always >$@

ANDROID_BUILD_TIME = $(ANDROID_ASSETS_DIRECTORY)/build.time
ANDROID_LOCAL_FILES += $(ANDROID_BUILD_TIME)
$(ANDROID_BUILD_TIME): FORCE
	date --utc +'%Y-%m-%d@%H:%M %Z' >$@

include ../../epilogue.mk

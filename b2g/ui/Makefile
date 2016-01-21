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

XML_VERSION_RESOURCE = res/values/version.xml
LOCAL_FILES += $(XML_VERSION_RESOURCE)
$(XML_VERSION_RESOURCE)::
	./mkversion >$@

ANDROID_FILES += custom_rules.xml
ANDROID_FILES += signing.properties

apk::
	$(MAKE) -C ../liblouis local-files native

include ../../epilogue.mk

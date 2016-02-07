include ../../prologue.mk
include ../../properties.mk

ACTION_SUMMARIES = res/values/actions.xml
LOCAL_FILES += $(ACTION_SUMMARIES)
actions: $(ACTION_SUMMARIES)
$(ACTION_SUMMARIES)::
	./mkactions >$@

APPLICATION_NAME = B2G_UI
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

ANDROID_FILES += custom_rules.xml
ANDROID_FILES += signing.properties

apk::
	$(MAKE) -C ../liblouis local-files native
	$(MAKE) -C ../doc all

clean::
	$(MAKE) -C ../doc $@

include ../../epilogue.mk

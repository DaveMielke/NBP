include ../../setup.mk
include $(TOP)app.prologue.mk
include $(TOP)app.properties.mk

APPLICATION_NAME = B2G_UI
PACKAGE_PATH = org.nbp.b2g.ui

ACTION_SUMMARIES = res/values/actions.xml
LOCAL_FILES += $(ACTION_SUMMARIES)
actions: $(ACTION_SUMMARIES)
$(ACTION_SUMMARIES)::
	./mkactions >$@

PROJECT_LIBRARIES += ../liblouis
PROJECT_LIBRARIES += ../../common

NATIVE_SOURCES += utils.h
NATIVE_SOURCES += utils.c
NATIVE_SOURCES += fwver.c
NATIVE_SOURCES += uinput.c
NATIVE_SOURCES += keyboard.c
NATIVE_SOURCES += kbdmon.c
NATIVE_SOURCES += metec_flat20_ioctl.h
NATIVE_SOURCES += brldev.c

apk::
	$(MAKE) -C ../doc all

clean::
	$(MAKE) -C ../doc $@

include $(TOP)app.epilogue.mk

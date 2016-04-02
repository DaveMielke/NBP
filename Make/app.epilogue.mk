SIGNING_PROPERTIES_FILE = signing.properties
ANDROID_BUILD_MODE := $(strip $(if $(wildcard $(SIGNING_PROPERTIES_FILE)), release, debug))
show-build-mode:
	@echo $(ANDROID_BUILD_MODE)

ifneq ($(words $(PROJECT_LIBRARIES)),0)
$(PROJECT_LIBRARIES): FORCE
	$(MAKE) -C $@ local-files native
endif

ifeq ($(words $(wildcard $(ANDROID_NATIVE_DIRECTORY))),0)
native:
	@echo no native code
else
ANDROID_NATIVE_SOURCES := $(NATIVE_SOURCES:%=$(ANDROID_NATIVE_DIRECTORY)/%)
ANDROID_NATIVE_LIBRARY = $(ANDROID_PLATFORM_DIRECTORY)/lib$(APPLICATION_NAME).so
native: $(ANDROID_NATIVE_LIBRARY)
$(ANDROID_NATIVE_LIBRARY): $(ANDROID_NATIVE_SOURCES)
	ndk-build
endif

local-files: $(LOCAL_FILES)

ANDROID_PROJECT_PACKAGE = $(ANDROID_BINARIES_DIRECTORY)/$(APPLICATION_NAME)-$(ANDROID_BUILD_MODE).apk
apk:: $(ANDROID_PROJECT_PACKAGE)
$(ANDROID_PROJECT_PACKAGE): native local-files $(PROJECT_FILES) $(PROJECT_LIBRARIES)
	ant $(ANDROID_BUILD_MODE)

clean::
	-rm -f $(LOCAL_FILES)
	-rm -f -r $(ANDROID_BINARIES_DIRECTORY)
	-rm -f -r $(ANDROID_PLATFORM_DIRECTORY)
	-rm -f -r gen
	-rm -f -r obj

install: all
	adb install $(ANDROID_PROJECT_PACKAGE)

reinstall: all
	adb install -r $(ANDROID_PROJECT_PACKAGE)

uninstall:
	adb uninstall $(PACKAGE_PATH)

FORCE:


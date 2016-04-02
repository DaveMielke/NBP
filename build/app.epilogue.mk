SIGNING_PROPERTIES_FILE = signing.properties
ANDROID_BUILD_MODE := $(strip $(if $(wildcard $(SIGNING_PROPERTIES_FILE)), release, debug))
show-build-mode:
	@echo $(ANDROID_BUILD_MODE)

local-files: $(LOCAL_FILES)
REQUIRED_TARGETS += local-files

show-local-files:
	@echo $(LOCAL_FILES)

LIBRARY_PROJECTS := $(shell sed -n -e 's/^ *android\.library\.reference\.[1-9][0-9]* *= *//p' <project.properties)
show-library-projects:
	@echo $(LIBRARY_PROJECTS)

ifneq ($(words $(LIBRARY_PROJECTS)),0)
library-projects: $(LIBRARY_PROJECTS)
REQUIRED_TARGETS += library-projects

$(LIBRARY_PROJECTS): FORCE
	$(MAKE) -C $@ required-targets
endif

ifneq ($(words $(wildcard $(ANDROID_NATIVE_DIRECTORY))),0)
ANDROID_NATIVE_LIBRARY = $(ANDROID_PLATFORM_DIRECTORY)/lib$(APPLICATION_NAME).so
native-library: $(ANDROID_NATIVE_LIBRARY)
REQUIRED_TARGETS += native-library

ANDROID_NATIVE_SOURCES := $(NATIVE_SOURCES:%=$(ANDROID_NATIVE_DIRECTORY)/%)
$(ANDROID_NATIVE_LIBRARY): $(ANDROID_NATIVE_SOURCES)
	ndk-build
endif

required-targets: $(REQUIRED_TARGETS)
show-required-targets:
	@echo $(REQUIRED_TARGETS)

ANDROID_PROJECT_PACKAGE = $(ANDROID_BINARIES_DIRECTORY)/$(APPLICATION_NAME)-$(ANDROID_BUILD_MODE).apk
apk: $(ANDROID_PROJECT_PACKAGE)
$(ANDROID_PROJECT_PACKAGE): required-targets $(PROJECT_FILES)
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


APPLICATION_NAME = UserInterface
PACKAGE_PATH = org.nbp.b2g.ui
PLATFORM_NAME = armeabi

JNI_SOURCES := Android.mk
JNI_SOURCES += utils.h
JNI_SOURCES += utils.c
JNI_SOURCES += uinput.c
JNI_SOURCES += kbddev.c
JNI_SOURCES += kbdmon.c
JNI_SOURCES += metec_flat20_ioctl.h
JNI_SOURCES += braille.c

ANDROID_BUILD_MODE := $(strip $(if $(wildcard signing.properties), release, debug))

ANDROID_JNI_DIRECTORY = jni
ANDROID_ASSETS_DIRECTORY = assets
ANDROID_BINARIES_DIRECTORY = bin
ANDROID_LIBRARIES_DIRECTORY = libs

ANDROID_PROJECT_NAME = NBP_B2G_$(APPLICATION_NAME)
ANDROID_PLATFORM_DIRECTORY = $(ANDROID_LIBRARIES_DIRECTORY)/$(PLATFORM_NAME)
ANDROID_JNI_SOURCES := $(JNI_SOURCES:%=$(ANDROID_JNI_DIRECTORY)/%)

ANDROID_PROJECT_PACKAGE = $(ANDROID_BINARIES_DIRECTORY)/$(ANDROID_PROJECT_NAME)-$(ANDROID_BUILD_MODE).apk
ANDROID_NATIVE_LIBRARY = $(ANDROID_PLATFORM_DIRECTORY)/lib$(APPLICATION_NAME).so
ANDROID_LOCAL_FILES = local.properties

all: apk
apk: $(ANDROID_PROJECT_PACKAGE)
jni: $(ANDROID_NATIVE_LIBRARY)

$(ANDROID_PROJECT_PACKAGE): jni $(ANDROID_LOCAL_FILES) AndroidManifest.xml key-store
	ant $(ANDROID_BUILD_MODE)

$(ANDROID_NATIVE_LIBRARY): $(ANDROID_JNI_SOURCES)
	ndk-build

$(ANDROID_LOCAL_FILES): FORCE
	android update project --path .

show-build-mode:
	@echo $(ANDROID_BUILD_MODE)

clean::
	-rm -f $(ANDROID_LOCAL_FILES)
	-rm -f -r $(ANDROID_BINARIES_DIRECTORY)
	-rm -f -r $(ANDROID_LIBRARIES_DIRECTORY)
	-rm -f -r gen

install: all
	adb install $(ANDROID_PROJECT_PACKAGE)

reinstall: all
	adb install -r $(ANDROID_PROJECT_PACKAGE)

uninstall:
	adb uninstall $(PACKAGE_PATH)

FORCE:

ifeq "$(ANDROID_BUILD_MODE)" "release"
include signing.properties

key.directory := ${dir ${key.store}}
key.archive = ${key.directory}${key.alias}.p12
key.certificate = ${key.directory}${key.alias}.x509.pem
key.encoded = ${key.directory}${key.alias}.pem
key.raw = ${key.directory}${key.alias}.pk8

key-encoded: ${key.encoded}
key-archive: ${key.archive}
key-store: ${key.store}

${key.encoded}: ${key.raw}
	openssl pkcs8 \
	-in "${key.raw}" \
	-passin "pass:${key.alias.password}" \
	-inform "DER" \
	-out "${key.encoded}" \
	-passout "pass:${key.alias.password}" \
	-outform "PEM"

${key.archive}: ${key.encoded}
	openssl pkcs12 -export \
	-in "${key.certificate}" \
	-inkey "${key.encoded}" \
	-passin "pass:${key.alias.password}" \
	-name "${key.alias}" \
	-out "${key.archive}" \
	-passout "pass:${key.alias.password}"

${key.store}: ${key.archive}
	keytool -importkeystore \
	-destkeystore "${key.store}" \
	-deststoretype "JKS" \
	-deststorepass "${key.store.password}" \
	-srckeystore "${key.archive}" \
	-srcstoretype "PKCS12" \
	-srcstorepass "${key.alias.password}" \
	-srckeypass "${key.alias.password}" \
	-alias "${key.alias}"

else
key-store:
	true
endif

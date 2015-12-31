include config.mk

KEY_RAW_EXTENSION = pk8
KEY_ENCODED_EXTENSION = pem
KEY_CERTIFICATE_EXTENSION = x509.pem
KEY_ARCHIVE_EXTENSION = p12
KEY_STORE_EXTENSION = jks

STORE_FILE = $(STORE_NAME).$(KEY_STORE_EXTENSION)

list:
	keytool -list -v \
	-keystore "$(STORE_FILE)" \
	-storepass "$(STORE_PASSWORD)"

DISTINGUISHED_NAME = CN=$(COMMON_NAME), OU=$(ORGANIZATION_UNIT), O=$(ORGANIZATION_NAME), L=$(ORGANIZATION_CITY), ST=$(ORGANIZATION_STATE), C=$(ORGANIZATION_COUNTRY)

key:
	keytool -genkey -v \
	-keystore "$(STORE_FILE)" \
	-storepass "$(STORE_PASSWORD)" \
	-storetype "JKS" \
	-alias "$(STORE_NAME)" \
	-keypass "$(KEY_PASSWORD)" \
	-keyalg RSA \
	-keysize 2048 \
	-validity 10000 \
	-dname "$(DISTINGUISHED_NAME)"

SYSTEM_KEY_NAMES = media platform release shared
system-keys: $(foreach key,$(SYSTEM_KEY_NAMES),$(key)-key)

%.$(KEY_ENCODED_EXTENSION): $(SOURCE_DIRECTORY)/%.$(KEY_RAW_EXTENSION)
	openssl pkcs8 \
	-in "$<" \
	-passin "pass:$(KEY_PASSWORD)" \
	-inform "DER" \
	-out "$@" \
	-passout "pass:$(KEY_PASSWORD)" \
	-outform "PEM"

%.$(KEY_ARCHIVE_EXTENSION): %.$(KEY_ENCODED_EXTENSION) $(SOURCE_DIRECTORY)/%.$(KEY_CERTIFICATE_EXTENSION)
	openssl pkcs12 -export \
	-in "$(word 2,$+)" \
	-inkey "$<" \
	-passin "pass:$(KEY_PASSWORD)" \
	-name "$(basename $(@F))" \
	-out "$@" \
	-passout "pass:$(KEY_PASSWORD)"

%-key: %.$(KEY_ARCHIVE_EXTENSION)
	keytool -importkeystore \
	-srckeystore "$<" \
	-srcstoretype "PKCS12" \
	-srcstorepass "$(KEY_PASSWORD)" \
	-srckeypass "$(KEY_PASSWORD)" \
	-destkeystore "$(STORE_FILE)" \
	-deststoretype "JKS" \
	-deststorepass "$(STORE_PASSWORD)" \
	-alias "$(basename $(<F))" \
	-noprompt

clean:
	-rm -f -- *.$(KEY_RAW_EXTENSION)
	-rm -f -- *.$(KEY_ENCODED_EXTENSION)
	-rm -f -- *.$(KEY_ARCHIVE_EXTENSION)


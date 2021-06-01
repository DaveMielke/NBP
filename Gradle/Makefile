all: calculator editor navigator
FORCE:

GRADLE_WRAPPER_COMMAND = ./gradlew -q

common liblouis duxbury: FORCE
	$(GRADLE_WRAPPER_COMMAND) :$(@):assembleDebug

calculator editor navigator: FORCE
	$(GRADLE_WRAPPER_COMMAND) :$(@):assembleRelease

install-%: %
	$(GRADLE_WRAPPER_COMMAND) :$^:installRelease


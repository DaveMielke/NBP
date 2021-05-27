all: common
FORCE:

GRADLE_WRAPPER_COMMAND = ./gradlew -q

common liblouis duxbury: FORCE
	$(GRADLE_WRAPPER_COMMAND) :$(@):assembleDebug

editor: FORCE
	$(GRADLE_WRAPPER_COMMAND) :$(@):assembleDebug


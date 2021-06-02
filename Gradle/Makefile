GRADLE_WRAPPER_COMMAND = ./gradlew -q

all: calculator editor navigator

calculator: calculator-app
editor: editor-app
navigator: navigator-app
calculator-app editor-app navigator-app:
	$(GRADLE_WRAPPER_COMMAND) :$(@:-app=):assembleRelease

common: common-lib
duxbury: duxbury-lib
liblouis: liblouis-lib
common-lib duxbury-lib liblouis-lib:
	$(GRADLE_WRAPPER_COMMAND) :$(@:-lib=):assembleDebug

install-%: %-app
	$(GRADLE_WRAPPER_COMMAND) :$(^:-app=):installRelease


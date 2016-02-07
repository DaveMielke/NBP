DOCUMENT_NAMES = UserInterface

all: html txt
LOCAL_FILES :=

BUILD_PROPERTIES = properties.rst
LOCAL_FILES += $(BUILD_PROPERTIES)
properties: $(BUILD_PROPERTIES)
$(BUILD_PROPERTIES):
	./mkproperties >$@

html: $(DOCUMENT_NAMES:=.html)
txt: $(DOCUMENT_NAMES:=.txt)

%.html: %/main.rst $(LOCAL_FILES)
	rst2html --config docutils.conf $< $@

%.txt: %.html
	lynx -dump -stderr -width=10000 -dont_wrap_pre -nolist -nonumbers $< >$@
	sed -e '/^ *Contents *$$/,/^ *$$/d' -i $@
#	links -dump -dump-charset utf-8 -no-references -no-numbering $< >$@
#	html2text -rcfile html2text.conf -nobs -o $@ $<

clean:
	-rm -f -- *.html
	-rm -f -- *.txt
	-rm -f -- $(LOCAL_FILES)


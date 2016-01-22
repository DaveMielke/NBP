DOCUMENT_NAMES = UserInterface

all: html txt

BUILD_PROPERTIES = properties.rst
properties: $(BUILD_PROPERTIES)
$(BUILD_PROPERTIES):
	./mkproperties >$@

html: $(DOCUMENT_NAMES:=.html)
txt: $(DOCUMENT_NAMES:=.txt)

%.html: %/main.rst properties
	rst2html --config docutils.conf $< $@

%.txt: %.html
	lynx -dump -stderr -width=10000 -dont_wrap_pre -nolist -nonumbers $< >$@
	sed -e '/^ *Contents *$$/,/^ *$$/d' -i $@
#	links -dump -dump-charset utf-8 -no-references -no-numbering $< >$@
#	html2text -rcfile html2text.conf -nobs -o $@ $<

clean:
	-rm -f -- *.html
	-rm -f -- *.txt
	-rm -f -- $(BUILD_PROPERTIES)


DOCUMENT_NAMES = b2g-ui-guide

all: html txt

html: $(DOCUMENT_NAMES:=.html)
txt: $(DOCUMENT_NAMES:=.txt)

%.html: %.rst
	rst2html --config docutils.conf $< $@

%.txt: %.html
	lynx -dump -stderr -nomargins -dont_wrap_pre -nolist -nonumbers $< >$@
#	links -dump -dump-charset utf-8 -no-references -no-numbering $< >$@
#	html2text -rcfile html2text.conf -nobs -o $@ $<

clean:
	-rm -f -- *.html
	-rm -f -- *.txt


DOCUMENT_NAMES = b2g-ui-guide

all: html txt

html: $(DOCUMENT_NAMES:=.html)
txt: $(DOCUMENT_NAMES:=.txt)

%.html: %.rst
	rst2html $< $@

%.txt: %.html
	lynx -dump -nomargins -nolist -nonumbers $< >$@

clean:
	-rm -f -- *.html
	-rm -f -- *.txt


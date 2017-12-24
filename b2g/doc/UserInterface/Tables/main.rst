Tables
======

General Tables
--------------

Supported Diacritical Marks
~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. include:: general-diacritics.rst

Braille Tables
--------------

Braille Characters
~~~~~~~~~~~~~~~~~~

Dot Numbering
`````````````

.. include:: braille-dots.rst

Unicode Braille Patterns
````````````````````````

Actual braille cells are represented by the 256 characters within the Unicode
range U+2800 through U+28FF. Each of the eight bits within the low-order byte
of this range represents a braille dot within the cell, according to
ISO 11548-1, as follows:

.. include:: braille-unicode.rst

Computer Braille Characters
~~~~~~~~~~~~~~~~~~~~~~~~~~~

These tables show how US English braille characters are represented
when `Computer Braille`_ is being used.
This mapping is also used for locales that don't (yet) have their own.

North American Braille Computer Code
````````````````````````````````````

The North American convention for how the 95 basic ASCII characters are
rendered in computer (8-dot) braille.

.. include:: braille-nabcc.rst

Additional Common Symbols
`````````````````````````

The representations of these additional symbols have been designed such that
they can be relatively easily remembered. The following methods have been used:

* Adding dot 8 to a related letter, e.g. adding dot 8 to the letter ``c`` for
  the copyright sign.

* Adding dot 7 to a related punctuation symbol, e.g. adding dot 7 to the dollar
  sign for the cent sign.

* The superscript digits are rendered by adding dot 7 to the representations of
  the corresponding regular digits.

* The subscript digits are rendered by adding dot 8 to the representations of
  the corresponding regular digits.

.. include:: braille-symbols.rst

Additional Characters for Spanish
`````````````````````````````````

The representations of these Spanish characters have been designed such that
they can be easily recognized. The following methods have been used:

* The lowercase accented letters are rendered by adding dot 8 to their six-dot
  Spanish literary braille representations.

* The uppercase accented letters are rendered by adding dots 7 and 8 to their
  six-dot Spanish literary braille representations.

* The inverted punctuation symbols are rendered by adding dot 7 to the
  representations of the associated `North American Braille Computer Code`_
  punctuation symbols.

.. include:: braille-spanish.rst

ASCII Control Characters
````````````````````````

To type a control character, press Space+x (dots 1346) immediately before any
of these characters:

.. include:: braille-control.rst

Speech Tables
-------------

English Phonetic Alphabet
~~~~~~~~~~~~~~~~~~~~~~~~~

.. include:: speech-phonetic.rst


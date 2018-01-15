Braille Rendering
-----------------

Characters
~~~~~~~~~~

Braille Modes and Codes
```````````````````````

Literary Braille
''''''''''''''''

Space+6 (dots 235) selects literary braille (six dot) mode.
It's the default, and is typically used for:

* Reading books.

Characters are rendered using the currently selected braille code.
Space+Dots78+g (dots 1245) displays a message (see `Messages`_) that confirms
which braille code has been selected.
Space+Dot8+g (dots 1245) selects the next braille code,
and Space+Dot7+g (dots 1245) selects the previous braille code.
The default braille code is UEB Grade 2.

The following braille codes are supported:

* English

  + UEB (Unified English Braille) grades 1 and 2.
  + EBAE (English Braille, American Edition) grades 1 and 2.

Highlighted words and characters are supported when not within an input area
(see `Input Areas`_)
insofar as the tables that define the current braille code support them.
The types of highlighting supported by the |user interface| are:
bold, italics, and underlining.

.. |LibLouis home page URL| replace:: http://liblouis.org/

We'd like to take this opportunity to publicly thank the LibLouis project
(see |LibLouis home page URL|)
as it's the package that's being used by the |user interface|
to perform translation to/from literary braille.

Computer Braille
''''''''''''''''

Space+8 (dots 236) selects computer braille (eight dot) mode.
It's typically used for:

* Computer programming.

* When there's a need for a strict one-to-one correspondence
  between a character and its braille representation,
  e.g. to verify column alignment.

The basic characters are rendered using the `North American Braille Computer
Code`_. Internally-designed representations for `Additional Common Symbols`_,
as well as for `Additional Characters for Spanish`_, have also been defined.

If the braille representation for a complex character (e.g. a letter with an
accent) hasn't been defined, then its base character (i.e. just the letter) is
displayed (unless, of course, even the base character's representation hasn't
been defined).  

The special character rendered as |the undefined character| is used to
represent a character that doesn't have a defined representation.

Actual Braille Cells
````````````````````

The characters that render in text as actual braille cells (see `Unicode
Braille Patterns`_) needn't (and, in fact, shouldn't) be defined. They're
always rendered in braille as themselves.

Identifying an Unrecognized Character
`````````````````````````````````````

If you encounter a character that you don't recognize, then you can find out
what it is by holding Dot7 while pressing the cursor routing key behind that
character. This will cause a description of the character to be presented
within a pop-up (see `Pop-ups`_). For example, the description for the letter
``s`` is::

  latin small letter s
  Code Point: U+0073
  Block: basic latin
  Category: lowercase letter
  Directionality: left to right

Theme Description
~~~~~~~~~~~~~~~~~

Special Symbols
```````````````

If a screen element (list, group of pages, etc) needs to be scrolled, then:

* Scrolling forward (down or to the right) is indicated via
  the pictorial symbol |forward scroll symbol|
  followed by the words ``forward scroll``.

* Scrolling backward (up or to the left) is indicated via
  the pictorial symbol |backward scroll symbol|
  followed by the words ``backward scroll``.

A **checkbox** is rendered as
either |the unchecked checkbox symbol| (meaning unchecked)
or |the checked checkbox symbol| (meaning checked),
followed by its label. For example::

  ⣏⠀⣹ This box is not checked.
  ⣏⠶⣹ This box is checked.

A **switch** is rendered as though it were a **checkbox**. The box is checked
if the switch is in its **on** position, and unchecked if it's in its **off**
position. For example::

  ⣏⠀⣹ Off
  ⣏⠶⣹ On

If a screen element has no text of its own,
but its developer has provided a textual description of it,
then that description is displayed.
For example::

  More options
  Navigate up

If meaningful text for a screen element can't be found then it is
rendered as its widget type enclosed within {braces}. For example::

  {ImageButton}
  {SeekBar}

Descriptive Annotations
```````````````````````

If a control isn't enabled then the word ``disabled``, enclosed within
(parentheses), is appended to its descriptive text. For example::

  OK (disabled)

When an action is performed that's neither implicitly confirmed (by an 
expected change on the braille display) nor explicitly confirmed (by a 
sound) then it's confirmed by a message (see `Messages`_). For example::

  One Hand On
  Long Press Off

Whenever you move onto a password field, a message (see `Messages`_) saying::

  password field

is displayed.
Also, |the password character| is displayed for each character you type within it.

Input Area Indicators
`````````````````````

Indicators are used to show where important parts of an input area
(see `Input Areas`_) are. They are dot patterns that are superimposed
(or overlaid) onto the characters that they're identifying.

The Cursor Indicator
''''''''''''''''''''

This indicator is superimposed onto the character where the cursor is.
It's default representation is |the cursor indicator|.
This can be changed via `The Settings Screen`_.

The actual cursor is a zero-width visual symbol that appears in between two
characters such that the next character typed is inserted just to its right.
Since this style of cursor representation can't be used in braille, the
|user interface| places its cursor indicator on the character that's just to
the right of the actual cursor.

The cursor isn't shown when at least one character has been selected
(see `Text Selection`_). This is because editing actions are then performed on
all of the selected characters as a single unit rather than at the cursor's
location.

The Selection Indicator
'''''''''''''''''''''''

This indicator serves two purposes:

* If text has been selected then it's superimposed onto all of those characters.

* If text hasn't been selected then it's superimposed onto each character
  that's highlighted (bold, italic, strike-through, and/or underlined).
  You can find out exactly how a character has been highlighted
  by holding Dot3 while pressing the cursor routing key associated with it.

It's default representation is |the selection indicator|.
This can be changed via `The Settings Screen`_.


Input Areas
-----------

The character where the cursor is is highlighted via |the cursor indicator|.

Pressing a cursor routing key brings the cursor to that character.
|if selection| the selection is cleared.

An additional blank character is rendered at the end of each line within an
input area. This character represents the delimiter between a line and the one
that follows it.

* Typing while the cursor is on it appends characters to the line.

* Deleting it joins the line to the start of the next one.

When a character is typed (see `Typing Characters`_), then:

* |if cursor| the typed character is inserted just to the
  left of the cursor.

* |if selection| the typed character replaces the selected
  text.

The Enter Key within an Input Area
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The Enter key is Dot8. If the input area supports more than one line then this
key ends the current line and starts a new one.

* Pressing it when at the start of a line inserts an empty line just above it.

* Pressing it when in the middle of a line splits that line just to the left of
  the cursor.

* Pressing it when at the end of a line starts a new line just below it.

Deleting Characters
~~~~~~~~~~~~~~~~~~~

The Backspace key is Dot7.

* |if cursor| the character just to the left of the
  cursor is deleted. If this key is pressed when at the start of a line then
  that line is joined to the end of the previous one.

* |if selection| the selected text is deleted.

The Delete key is Space+d (dots 145).

* |if cursor| the character where the cursor is is
  deleted. If this key is pressed when at the end of a line then that line is
  joined to the start of the next one.

* |if selection| the selected text is deleted.

Deleting Words
~~~~~~~~~~~~~~

These actions only work when there's a cursor.
|if selection| they don't do anything.

* Add Dot7 to Space+d to delete the previous word.
* Add Dot8 to Space+d to delete the next (or current) word.

If the cursor is on space then all of that space is also deleted.
If the cursor is within a word (not on its first character)
then, rather than deleting the previous/next word,
these actions delete the start/end of the current word.
Here are the specifics:

Pressing Space+Dot7+d (dots 145) deletes the previous word.

* If the cursor is on a word, but not on its first character,
  then the characters of that word to the left of the cursor are removed.

* If the cursor is on the first character of a word,
  or if it's on space after a word,
  then the previous word, along with all the space after it, is removed.

* If the cursor is on space at the start of a line
  then all of that space is removed.

Pressing Space+Dot8+d (dots 145) deletes the next word.

* If the cursor is on the first character of a word
  then that word, along with all the space after it, is removed.

* If the cursor is on any other character (except the first) of a word
  then the characters of that word to the right of the cursor are removed.

* If the cursor is on space before a word
  then that word, along with all the space before it, is removed.

* If the cursor is on space at the end of a line
  then all of that space is removed.

Panning within an Input Area
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The Forward and Backward keys pan through an input area in the expected way:

* The Forward key pans to the right, and, at the end of each line,
  wraps to the start of the next one. At the end of the last line
  it moves to the start of the next screen element.
  
* The Backward key pans to the left, and, at the start of each line,
  wraps to the end of the previous one. At the start of the first line
  it moves to the start of the previous screen element.

Pressing Space+Dots17 pans the braille display
such that its first character corresponds to:

* |if cursor| the cursor's location.
* |if selection| the first selected character.

Pressing Space+Dots48 pans the braille display
such that its last character corresponds to:

* |if cursor| the cursor's location.
* |if selection| the last selected character.

The Directional Keys within an Input Area
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. |directional move to short line| replace::
  If that line is too short then the cursor is also moved leftward.

The directional keys move the cursor through the text, one step at a time. They
will not leave the input area. The braille display is panned, as needed, such
that the cursor is always visible.

The Left key moves the cursor to the previous character. If it's at the start
of a line then it wraps to the end of the previous line.
|if selection| the cursor is moved to just before the first selected character,
and the selection is cleared.

The Right key moves the cursor to the next character. If it's at the end of a
line then it wraps to the start of the next line. |if selection|
the cursor is moved to just after the last selected character, and the
selection is cleared.

The Up key moves the cursor to the same position on the previous line.
|directional move to short line|
|if selection| the cursor is moved to just above the first
selected character, and the selection is cleared.

The Down key moves the cursor to the same position on the next line.
|directional move to short line|
|if selection| the cursor is moved to just below the last
selected character, and the selection is cleared.

Traditional Key Combinations within an Input Area
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The |user interface| supports input area navigation via dot key chords
|traditional key combinations|.

.. include:: input-traditional.rst

Password Fields
~~~~~~~~~~~~~~~

If the input area is a password field
(one in which the characters being typed don't appear)
then the |user interface| automatically switches to `Computer Braille`_
(see `Typing in Computer Braille`_).
This is done because it's extremely difficult
to accurately enter a password in `Literary Braille`_, especially
when it contains special characters
and/or when a braille code that defines contractions is being used.

The message (see `Messages`_)::

  password field
 
is displayed whenever you move onto a password field in order to alert you
that you won't be able to read what you're typing
and that you need to be `typing in computer braille`_.

Android has an accessibility setting named Speak Passwords
that affects how what you type within a password field is rendered.
If it's disabled (the default) then each character within a password field
is rendered as |the password character|.
If it's enabled then password fields are rendered in plain text.
While enabling this setting makes it easy to be sure that a password
has been typed correctly, be aware that,
in addition to being on the braille display (which can be read privately),
if speech is enabled then it's also spoken (which can be overheard by others).

Protecting an Input Area
~~~~~~~~~~~~~~~~~~~~~~~~

You can protect an input area against inadvertent modification
(accidentally typing, etc)
by disabling input area editing.
This is done by pressing Space+Dot7+e (dots 15).
Pressing Space+Dot8+e (dots 15) reenables it.
When disabled, any action that would normally modify an input area
results in a message (see `Messages`_) that input editing has been disabled.


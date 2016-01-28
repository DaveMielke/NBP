Input Areas
-----------

The character where the cursor is is highlighted via |the cursor indicator|.

Pressing a cursor routing key brings the cursor to that character. If 
text has been selected then the selection is cleared.

An additional blank character is rendered at the end of each line within an
input area. This character represents the delimiter between a line and the one
that follows it.

* Typing while the cursor is on it appends characters to the line.

* Deleting it joins the line to the start of the next one.

When a character is typed (see `Typing Characters`_), then:

* If text hasn't been selected then the typed character is inserted just to the
  left of the cursor.

* If text has been selected then the typed character replaces the selected
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

* If text hasn't been selected then the character just to the left of the
  cursor is deleted. If this key is pressed when at the start of a line then
  that line is joined to the end of the previous one.

* If text has been selected then the selected text is deleted.

The Delete key is Space+d (dots 145).

* If text hasn't been selected then the character where the cursor is is
  deleted. If this key is pressed when at the end of a line then that line is
  joined to the start of the next one.

* If text has been selected then the selected text is deleted.

Panning within an Input Area
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The Forward and Backward keys pan through an input area in the expected way,
but won't leave it. The Forward key pans to the right, and, at the end of each
line, wraps to the start of the next one. The Backward key pans to the left,
and, at the start of each line, wraps to the end of the previous one. See
`Leaving the Current Screen Element`_ for how to move to another screen
element.

Pressing Space+Dots17 pans the braille display such that
its first character corresponds to the cursor's location,
or, if text has been selected, to the start of the selection.

Pressing Space+Dots48 pans the braille display such that
its last character corresponds to the cursor's location,
or, if text has been selected, to the end of the selection.

The Directional Keys within an Input Area
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. |directional move to short line| replace::
  If that line is too short then the cursor is also moved leftward.

The directional keys move the cursor through the text, one step at a time. They
will not leave the input area. The braille display is panned, as needed, such
that the cursor is always visible.

The Left key moves the cursor to the previous character. If it's at the start
of a line then it wraps to the end of the previous line. If text has been
selected then the cursor is moved to just before the first selected character,
and the selection is cleared.

The Right key moves the cursor to the next character. If it's at the end of a
line then it wraps to the start of the next line. If text has been selected
then the cursor is moved to just after the last selected character, and the
selection is cleared.

The Up key moves the cursor to the same position on the previous line.
|directional move to short line|
If text has been selected then the cursor is moved to just above the first
selected character, and the selection is cleared.

The Down key moves the cursor to the same position on the next line.
|directional move to short line|
If text has been selected then the cursor is moved to just below the last
selected character, and the selection is cleared.


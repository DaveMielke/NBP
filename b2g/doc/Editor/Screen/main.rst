The Main Screen
===============

The main screen contains the following screen elements:

The Menu Button
---------------

This button goes to `The Actions Menu`_.
Another way to do this is to press Space+m (dots 134).

The Current Path
----------------

This region shows the absolute path to the file that's currently being edited.
If it's a new file (see the `New`_ action)
then it says::

  [new file - no path]

The Edit Area
-------------

This region is where the content of the file is loaded and can be modified.
If the file is empty then it says::

  [edit area - empty file]

Braille Indicators
~~~~~~~~~~~~~~~~~~

Dots 78 [⣀] represents the cursor.
This is the location where new characters are inserted
and where existing characters are deleted.

Dot 8 [⢀] represents
either selected text (see `Selecting Text`_)
or highlighted text (see `Highlighting Text`_).

Navigation Operations
~~~~~~~~~~~~~~~~~~~~~

Many of these operations are for browsing through the text.
Unless explicitly stated, the cursor isn't moved.

Forward
  Pan to the right on the current line.
  At the end of the current line, wrap to the start of the next line.

Backward
  Pan to the left on the current line.
  At the start of the current line, wrap to the end of the previous line.

Space+Dot3 (or Left)
  Move the cursor one character to the left.
  At the start of the current line, wrap to the end of the previous line.

Space+Dot6 (or Right)
  Move the cursor one character to the right.
  At the end of the current line, wrap to the start of the next line.

Space+Dot1 (or Up)
  Move the cursor one line up.

Space+Dot4 (or Down)
  Move the cursor one line down.

Space+Dots13 (or Space+Left)
  Pan to the start of the current line.

Space+Dots46 (or Space+Right)
  Pan to the end of the current line.

Space+Dots23 (or Space+Up)
  Pan to the start of the current paragraph.
  If on the first line of a paragraph
  then pan to the start of the previous paragraph.

Space+Dots56 (or Space+Down)
  Pan to the start of the next paragraph.

Space+Dots123
  Pan to the start of the first line.

Space+Dots456
  Pan to the end of the last line.

Dots17
  Pan to the start of the current text selection.
  If text hasn't been selected then pan to the cursor.

Dots48
  Pan to the end of the current text selection.
  If text hasn't been selected then pan to the cursor.

Any Cursor Routing Key
  Bring the cursor to the corresponding character.

Editing Operations
~~~~~~~~~~~~~~~~~~

Typing Any Character
  Insert that character immediately to the left of the cursor,
  and then move the cursor one character to the right.

  * To type a control character:

    1) Press Space+x (dots 1346).
    2) Type the corresponding letter.

  * To type any character by its Unicode value:

    1) Press Space+u (dots 136).
    2) Enter the hexadecimal representation of that character's Unicode value.
    3) Press Enter (dot 8).

Dot8
  The Enter key.

  * At the start of a line, insert a blank line above it.

  * At the end of a line, start a new line below it.

  * In the middle of a line, split that line
    just before the character where the cursor is.

Dot7
  The Backspace key.
  Delete the character to the left of the cursor.
  If text has been selected then delete all of it.

Space+d (dots 145)
  The Delete key.
  Delete the character where the cursor is.
  If text has been selected then delete all of it.

Selecting Text
~~~~~~~~~~~~~~

Press Backward together with a cursor routing key
to mark the start of a text selection.
If text has already been selected then its start is adjusted.

Press Forward together with a cursor routing key
to mark the end of a text selection.
If text has already been selected then its end is adjusted.

Press Space+a (dot 1) to select all of the text.

Press any cursor routing key (by itself)
to clear the text selection and set the cursor's location.

Clipboard Operations
~~~~~~~~~~~~~~~~~~~~

The editor doesn't have its own clipboard -
it uses Android's global clipboard.
This means that text can be easily copied between the editor and other apps.

Space+x (dots 1346)
  Cut (copy and then delete) the current text selection to the clipboard.

Space+c (dots 14)
  Copy the current text selection to the clipboard.
  If text hasn't been selected then copy all of the text to the clipboard.

Space+v (dots 1236)
  Paste (insert) what's on the clipboard into the text
  at the cursor's location.

Highlighting Text
~~~~~~~~~~~~~~~~~

Press Space + one of the following letters
together with Dot8 to turn on the corresponding type of highlighting,
and together with Dot7 to turn it off:

.. include:: highlight.rst

More than one type of highlighting may be turned on at the same time.
A quick way to turn all of them off at once
is to press Space+h (dots 125) together with Dot7.

If a character is marked as being highlighted (see `Braille Indicators`_)
then you can press Dot3 together with the corresponding cursor routing key
to find out how it has been highlighted.

Finding Text
~~~~~~~~~~~~

To find text:

1) Press Space+f (dots 124).
2) Enter the text.
3) Press Enter (dot 8).

The text is interpreted as a sequence of space-separated words.

* The amount of space between words doesn't matter.
  Any amount of space within the text
  matches any amount of space within the file.

* The first word within the text
  only needs to match the end of a word within the file.
  Enter a space before it to ensure that it matches a whole word.

* The last word within the text
  only needs to match the start of a word within the file.
  Enter a space after it to ensure that it matches a whole word.

A case-insensitive, forward search is performed.
If the text is found then the braille display is panned
such that its leftmost cell is where the text starts.

To continue searching forward for the same text without reentering it,
press Space+Dot8+f (dots 124).

To search backward for the same text without reentering it,
press Space+Dot7+f (dots 124).


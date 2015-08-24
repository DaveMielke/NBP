Navigation
==========

Emulating the Android Keys
--------------------------

All Android devices have a number of special keys. There are two ways to
emulate each of them:

* With two hands, by pressing Space together with a related letter or
  special symbol. While these may be easier to remember, they're also less
  convenient because both hands must be removed from the display.

* With one hand, by pressing Dot4 together with one of the D-Pad keys.

.. include:: tbl-keys-android.rst

Emulating Screen Gestures
-------------------------

To tap (or click) the current screen element, press Center. Another
(legacy) way to do it is to press `The Enter Key`_.

To hold (or long click) the current screen element, press Space+Center.

Navigating with the Forward and Backward Keys
---------------------------------------------

The easiest way to navigate the screen is to use the Forward and Backward 
keys because they move sequentially through all of the screen elements,
including those that merely present helpful text, without missing any of
them. The Forward key stops at the end of the screen, and the Backward key
stops at the start of the screen.

If the text of a screen element is longer than the braille display 
and/or has more than one line, then:

* The Forward key pans to the right, wrapping to the start of the next line as
  needed, such that all of the text is presented. When it reaches the end of
  the text, it moves to the start of the next screen element.

* The Backward key pans to the left, wrapping to the end of the previous line
  as needed, such that all of the text is presented. When it reaches the start
  of the text, it moves to the start of the previous screen element.

Repeatedly pressing Forward, therefore, reads through all of the text
on the screen because it reads through all of the text associated with the
current screen element before moving to the next one. Repeatedly pressing
Backward, however, pans to the left through the text associated with the
current screen element, but, from then on, moves directly to the start of each
successive preceding screen element.

Leaving the Current Screen Element
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The following methods may be used to force an immediate, direct move to the
start of the next (or previous) screen element:

* Press Space together with Forward (or Backward).

* Long press Forward (or Backward). See `Long Press Mode`_.

One of these methods must also be used in order to leave an input area (see
`Input Areas`_).

Reverse Panning Mode
~~~~~~~~~~~~~~~~~~~~

Reverse Panning Mode is designed for those who prefer to read with their right
hands while navigating with their left hands. When enabled, the Forward and
Backward keys are reversed insofar as navigation is concerned:

* Backward pans to the right, wraps to the start of the next line, moves to the
  next screen element, etc.

* Forward pans to the left, wraps to the end of the previous line, moves to the
  previous screen element, etc.
  
This mode is disabled by default. Pressing Forward+Dot5 enables it, and
pressing Backward+Dot2 disables it.

Explicit Panning
~~~~~~~~~~~~~~~~

The Forward and Backward keys pan right and left by the full length of the
braille display. This can make it difficult to read an indented block of
related text (a column, a long word, a timestamp, etc).

The braille display can be explicitly panned by holding Dot8 while pressing a
cursor routing key. This positions the braille display such that the rendered
portion of the current line starts with that character.

Navigating with the Directional Keys
------------------------------------

Up moves up through the lines of text for the current screen 
element, and, when on the top line, moves to the start of the text for
the previous screen element.

Down moves down through the lines of text for the current screen 
element, and, when on the bottom line, moves to the start of the text for
the next screen element.

Left pans to the left through the text for the current screen 
element, wrapping to the end of the previous line, and, when at the start of 
the text, moves to the start of the text for the previous screen element.

Right pans to the right through the text for the current screen 
element, wrapping to the start of the next line, and, when at the end of 
the text, moves to the start of the text for the next screen element.

Space+Up performs a scroll backward (up or left) operation.

* If within a list then move up several elements.
* If within a set of pages then move to the previous page.

Space+Down performs a scroll forward (down or right) operation.

* If within a list then move down several elements.
* If within a set of pages then move to the next page.

Space+Left moves to the first element of a list, to the first page of a group,
etc.

Space+Right moves to the last element of a list, to the last page of a group,
etc.

These keys act differently within some other contexts. For those details, see:

* `The Directional Keys within an Input Area`_
* `The Directional Keys When on a Slider`_

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

When a character is typed (see `Typing`_), then:

* If text hasn't been selected then the typed character is inserted just to the
  left of the cursor.

* If text has been selected then the typed character replaces the selected
  text.

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

The Forward and Backward keys pan through the input area in the expected way,
but won't leave it. The Forward key pans to the right, and, at the end of each
line, wraps to the start of the next one. The Backward key pans to the left,
and, at the start of each line, wraps to the end of the previous one. See
`Leaving the Current Screen Element`_ for how to move to another screen
element.

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

Selecting Text
--------------

Each character within the current text selection is highlighted via
|the selection indicator|. If text has been selected then the cursor isn't
shown.

Selecting text is useful in at least the following ways:

* Typing a character deletes the selected text, puts the cursor where
  the selected text was, and then inserts the typed character at that point.
  This, in other words, is an efficient way to replace old text with new text.
  Just select the old text, and then start typing the new text.

* Pressing any of the delete keys (see `Deleting Characters`_) removes all of
  the selected text. This, in other words, is an efficient way to delete a
  block of text. Just select it, and then delete it.

A quick way to select all of the characters within the input area is to press
Space+Dot8+a (dot 1).

Any subset of the characters within the input area can be selected by following
these steps:

1) If necessary, use the Forward and Backward keys to pan to a place where the
   first character to be selected can be seen.

2) Hold Backward while pressing the cursor routing key behind the 
   character that's to start the selection.

3) If necessary, use the Forward and Backward keys to pan to a place where the
   last character to be selected can be seen.

4) Hold Forward while pressing the cursor routing key behind the 
   character that's to end the selection.

Steps 1 and 2 may be reperformed at any time in order to change the start of
the selection. Likewise, steps 3 and 4 may be reperformed at any time in order
to change the end of the selection. In fact, the end of the selection can be
set before the start of the selection has been set, i.e. steps 3 and 4 may be
performed before steps 1 and 2 have been performed.

If the start of the selection is set first, and if the cursor is after that
character, then the selection is implicitly extended forward to (but not
including) the character where the cursor is. This, for example, provides an
easy way to delete or replace several characters that have just been typed.

If the end of the selection is set first, and if the cursor is before that
character, then the selection is implicitly extended back to (and including)
the character where the cursor is. This, for example, provides an easy way to
replace a word, line, paragraph, etc.

Using the Clipboard
-------------------

To copy text to the clipboard, press Space+Dot8+c (dots 14). If this is done
when not within an input area, or when within an input area with no selected
text, then all of the text is copied. If this is done within an input area with
selected text then just that text is copied.

To cut text to the clipboard, press Space+Dot8+x (dots 1346). This can only be
done within an input area. If text hasn't been selected then all of the text is
cut. If text has been selected then just that text is cut.

To paste text from the clipboard, press Space+Dot8+v (dots 1236). This can only
be done within an input area. If text hasn't been selected then the pasted text
is inserted just to the left of the cursor. If text has been selected then the
pasted text replaces the selected text.

Sliders
-------

A ``slider`` is a control that intuitively (from a visual perspective)
represents a numeric value (distance, magnitude, etc). It's often used, for
example, to represent a volume control, the current position within a song,
etc.

Each time the slider is adjusted, a brief message is displayed that 
announces its new position (as a percentage). Unfortunately, due to a 
current system limitation, its current position can't be determined (without
actually looking at it on the screen). It's necessary, therefore, to adjust its
position back and forth in order to figure out what it is.

Attempting to either increase a slider's position beyond its maximum or
decrease its position beyond its minimum neither displays a position message
nor indicates an error.

The Directional Keys When on a Slider
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

A slider is normally adjusted by tapping the desired point along it's bar.
This, of course, can't be done on a device that doesn't have a touch screen.
Instead, use the Left and Right directional keys (on the D-Pad) to adjust it.

* The Left key decreases its position.
* The Right key increases its position.
* The Up and Down keys don't do anything.

Finding Text within the Current Screen Element
----------------------------------------------

To find a sequence of words within the text that's associated with the current
screen element, press Space+f (dots 124). This brings up a prompt (see
`Prompts`_) with the following header::

  find>

Enter one or more words (sequences of non-space characters) separated by
spaces. The same words must occur together, in the same order, within the text.
You don't need to know how many spaces are between each pair of words within
the text because however many spaces you enter will match any number of spaces
within the text. You also don't need to know which letters are in uppercase and
which are in lowercase because the search isn't case sensitive.

The first word you enter need only match the end of the corresponding word
within the text. Likewise, the last word you enter need only match the start of
the corresponding word within the text. Enter a leading space to force the
first word to match an entire word. Likewise, enter a trailing space to force
the last word to match an entire word.

These same rules apply if you enter a single word. Since it's both the first
and the last word, it need only match part (the start, middle, or end) of a
word within the text. Enter a leading space to force it to match to start of a
word, a trailing space to force it to match the end of a word, and both to
force it to match an entire word.

You can edit the word(s) that you're entering. Press Enter when you're done.

A forward search through the text is performed. If the words are found then the
braille display is repositioned such that they begin at its leftmost character.

The following convenience key combinations have also been defined:

Space+Dot8+f (dots 124):
  Search forward through the text for the next match.

Space+Dot7+f (dots 124):
  Search backward through the text for the previous match.

Legacy Navigation
-----------------

Navigation used to be done by using real keys on an actual keyboard. This
method can still be used via an external keyboard connected via Bluetooth or
USB. The |user interface| also supports it by emulating the following keyboard
keys:

.. include:: tbl-keys-keyboard.rst

What each of these keys does is defined by Android - not by the
|user interface|. Here's a summary.

.. |describe cursor left navigation| replace::
  move to the nearest screen element that's roughly to the left of the
  current one

.. |describe cursor right navigation| replace::
  move to the nearest screen element that's roughly to the right of the
  current one

.. |describe cursor up navigation| replace::
  move to the nearest screen element that's roughly above the current one

.. |describe cursor down navigation| replace::
  move to the nearest screen element that's roughly below the current one

.. |move cursor to start| replace::
  move the cursor to the first selected character and then clear the
  selection

.. |move cursor to end| replace::
  move the cursor to the character just after it and then clear the
  selection

.. |cursor navigation is unreliable| replace::
  This operation is not only imprecise, but also doesn't find screen
  elements that merely present helpful text.

.. |describe vertical cursor motion within text| replace::
  Vertical cursor motion within text may cause it to also move unexpectedly
  left or right in braille, especially when a proportional font has been
  used, because the characters on the screen may not all have the same width.

The Enter Key
~~~~~~~~~~~~~

**Dot8**

* If on a button then press it.

* If on a checkbox then check/uncheck it.

* If on a switch then move it to its other position.

* If on a folder then open it.

* If on an app then go to it.

* If within an input area:

  + If text has been selected then replace it with a ``new line`` character.

  + If text hasn't been selected then insert a ``new line`` character to the
    left of the cursor.

The Cursor Left Key
~~~~~~~~~~~~~~~~~~~

**Space+Dot3**

* If not within an input area then |describe cursor left navigation|.
  |cursor navigation is unreliable|

* If within an input area:

  + If text hasn't been selected then move the cursor one character to the
    left. If it's at the start of a line then wrap to the end of the previous
    line.

  + If text has been selected then |move cursor to start|.

The Cursor Right Key
~~~~~~~~~~~~~~~~~~~~

**Space+Dot6**

* If not within an input area then |describe cursor right navigation|.
  |cursor navigation is unreliable|

* If within an input area:

  + If text hasn't been selected then move the cursor one character to the
    right. If it's at the end of a line then wrap to the start of the next
    line.

  + If text has been selected then |move cursor to end|.

The Cursor Up Key
~~~~~~~~~~~~~~~~~

**Space+Dot1**

* If not within an input area then |describe cursor up navigation|.
  |cursor navigation is unreliable|

* If within an input area:

  + If text hasn't been selected then move the cursor one line up. If it's on
    the top line then |describe cursor up navigation| (see above).
    |describe vertical cursor motion within text|

  + If text has been selected then |move cursor to start|.

The Cursor Down Key
~~~~~~~~~~~~~~~~~~~

**Space+Dot4**

* If not within an input area then |describe cursor down navigation|.
  |cursor navigation is unreliable|

* If within an input area:

  + If text hasn't been selected then move the cursor one line down. If it's on
    the bottom line then |describe cursor down navigation| (see above).
    |describe vertical cursor motion within text|

  + If text has been selected then |move cursor to end|.

The Page Up Key
~~~~~~~~~~~~~~~

**Space+Dots23**

Perform a scroll backward (up or to the left) operation.

* If within a list then move up several elements. If the first element is
  already visible then move to it.

* If within an input area then move up several lines. If the first line is
  already visible then move to it.

* If within a set of pages then move to the previous page.

The Page Down Key
~~~~~~~~~~~~~~~~~

**Space+Dots56**

Perform a scroll forward (down or to the right) operation.

* If within a list then move down several elements. If the last element is
  already visible then move to it.

* If within an input area then move down several lines. If the last line is
  already visible then move to it.

* If within a set of pages then move to the next page.

The Home Key
~~~~~~~~~~~~

**Space+Dots123**

Move to the first element of a list, to the start of a line of text, to the
first page of a group, etc.

The End Key
~~~~~~~~~~~

**Space+Dots456**

Move to the last element of a list, to the end of a line of text, to the last
page of a group, etc.

The Shift+Tab Key
~~~~~~~~~~~~~~~~~

**Space+Dots12**

Move to the previous screen element that can perform an action. If on the first
screen element then wrap to the last one.

The Tab Key
~~~~~~~~~~~

**Space+Dots45**

Move to the next screen element that can perform an action. If on the last
screen element then wrap to the first one.


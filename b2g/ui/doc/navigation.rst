Navigation
==========

Emulating the Android Keys
--------------------------

All Android devices have a number of special keys. There are two ways to
emulate each of them:

* With two hands, by pressing the Space key together with a related letter or
  special symbol. While these may be easier to remember, they're also more
  awkward to use because it's not possible to be reading the display at the
  same time.

* With one hand, by pressing the Dot4 key together with one of the D-Pad keys.

.. include:: tbl-keys-android.rst

Navigating with the Forward and Backward Keys
---------------------------------------------

The easiest way to navigate the screen is to use the Forward and Backward 
keys because they move sequentially through all of the screen elements,
including those that only contain descriptive text, without missing any of
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

Repeatedly pressing the Forward key, therefore, reads through all of the text
on the screen because it reads through all of the text associated with the
current screen element before moving to the next one. Repeatedly pressing the
Backward key, however, pans to the left through the text associated with the
current screen element, but, from then on, moves directly to the start of each
preceding screen element.

The following methods may be used to force an immediate, direct move to the
start of the next (or previous) screen element without first panning through
the text associated with the current one:

* Press the Space key together with the Forward (or Backward) key.

* Long press the Forward (or Backward) key (see `Long Press Mode`_).

These methods are also necessary in order to escape an input area.

Basic System Actions
--------------------

These key combinations perform basic system actions:

Space+f (dots 124)
  Find text within the current screen element (see `Find Mode`_).

Space+? (dots 1456)
  Go to the currently registered **assist** app. The default is
  ``Google Now``.

Space+Dots1478
  Go to the Power Off screen.

Center (on the D-Pad)
  Tap (click) the current screen element.

Space+Center
  Hold (long click) the current screen element.

Space+Dot8+c (dots 14)
  Copy all of the text to the clipboard.

Space+s (dots 234)
  Go to a Settings screen. Another key combination must be pressed
  in order to specify which Settings screen to go to:

  Space
    Go to the main Settings screen.

  a (dot 1)
    Go to the Accessibility Settings screen.

  b (dots 12)
    Go to the Bluetooth Settings screen.

  w (dots 2456)
    Go to the Wi-Fi Settings screen.

Find Mode
---------

Find mode is a prompt that allows a search string to be entered. When a 
character is typed, it is entered where the cursor is. In addition to 
typing characters, the search string can also be edited as described 
below.

Dot 8 is the Enter key. It returns to the system screen, searches 
forward through the current screen element for the text, and, if found, 
aligns the start of the braille display with the start of the text.

Dot 7 is the Backspace key. It deletes the character to the left of the 
cursor. A long press deletes all of the characters to the left of the
cursor.

Space+d (dots 145) is the Delete key. It deletes the character where the 
cursor is. A long press also deletes all of the characters to the right
of the cursor.

Pressing a cursor routing key brings the cursor to that character.

Pressing a cursor routing key in combination with the Space key scrolls 
the braille display to the right such that the visible portion of the 
prompt begins with that character.

The forward key pans the braille display to the right, and the Backward 
key pans it to the left.

Input Areas
-----------

Dots 1 through 6 are used, as defined by the `North American Braille 
Computer Code`_, to type characters, and the Space key is used to type a 
space. Add dot 7 to a letter to make it uppercase. If no characters have been
selected then, when a character is typed, it is inserted where the cursor is.
If characters have been selected then the next typed character replaces them.

Dot 8 is the Enter key. If the field supports more than one line, this 
key ends the current line and starts a new one.

Dot 7 is the Backspace key. It deletes the character to the left of the 
cursor. If characters have been selected then it deletes all of them.

Space+d (dots 145) is the Delete key. It deletes the character where the 
cursor is. If characters have been selected then it deletes all of them.

The Forward and Backward keys navigate through the text as defined 
previously, except that they will not leave the field. They must be long 
pressed in order to force an immediate move to the start of the next or 
previous field. Another way to force a move, which is especially 
applicable when secondary actions have been disabled, is to press the 
Space key together with the Forward or Backward key.

The four directional keys (of the D-Pad) move the cursor through the 
text, one step at a time. They will not leave the field. The braille 
display is panned, as needed, such that the cursor is always visible.

The Left key moves the cursor to the previous character of the current 
line. If the cursor is on the first character of the line then it wraps 
to the last character of the previous line. If characters are selected 
then the cursor is moved to just before the first selected character, 
and the character selection is cleared.

The Right key moves the cursor to the next character of the current 
line. If the cursor is on the last character of the line then it wraps 
to the first character of the next line. If characters are selected then 
the cursor is moved to just after the last selected character, and the 
character selection is cleared.

The Up key moves the cursor to the same column of the previous line. If 
the previous line is too short then the cursor is also moved leftward to 
just after its last character. If characters are selected then the 
cursor is moved to just above the first selected character, and the 
character selection is cleared.

The Down key moves the cursor to the same column of the next line. If 
the next line is too short then the cursor is also moved leftward to 
just after its last character. If characters are selected then the 
cursor is moved to just below the last selected character, and the 
character selection is cleared.

Pressing a cursor routing key brings the cursor to that character. If 
characters are selected then the character selection is cleared.

Pressing a cursor routing key in combination with the Backward key sets 
the first selected character, and pressing a cursor routing key in 
combination with the Forward key sets the last selected character. If 
either of these actions is performed while characters are already 
selected then the start or end of the selection is readjusted as 
requested. The sequence of selected characters may:

* Span multiple lines.
* Begin anywhere on its first line.
* End anywhere on its last line.

Pressing a cursor routing key in combination with the Space key scrolls 
the braille display to the right such that the visible portion of the 
current line begins with that character.

These key combinations perform actions within input areas:

Space+Dot8+a (dot 1)
  Select all of the text.

Space+Dot8+x (dots 1346)
  Cut the currently selected text to the clipboard.

Space+Dot8+c (dots 14)
  Copy the currently selected text to the clipboard. If no text is
  selected then all of the text is copied.

Space+Dot8+v (dots 1236)
  Paste the clipboard content into the text being edited. If no
  characters have been selected then the clipboard content is inserted
  where the cursor is. If characters have been selected then the
  clipboard content replaces them.

Text Selection
--------------

When within an input area, any sequence of text within that field 
can be easily **selected**. This is how to do it:

1) If necessary, use the Forward and Backward keys to pan to a place where the
   first character to be selected can be seen.

2) Hold the Backward key while pressing the cursor routing key behind the 
   character that's to start the selection.

3) If necessary, use the Forward and Backward keys to pan to a place where the
   last character to be selected can be seen.

4) Hold the Forward key while pressing the cursor routing key behind the 
   character that's to end the selection.

Steps 1 and 2 may be reperformed at any time in order to change the start of
the selection. Likewise, steps 3 and 4 may be reperformed at any time in order
to change the end of the selection. In fact, the end of the selection can be
set before the start of the selection has been set, i.e. steps 3 and 4 may be
performed before steps 1 and 2 have been performed.

The current text selection is highlighted via |the selection indicator|.
Selecting text is useful in at least the following ways:

* Typing a character deletes the selected text, puts the cursor where
  the selected text was, and then inserts the typed character at that point.
  This, in other words, is an efficient way to replace old text with new text.
  Just select the old text, and then start typing the new text.

* Pressing any of the delete keys (see `Input Areas`_) removes all of
  the selected text. This, in other words, is an efficient way to delete a
  block of text. Just select it, and then delete it.

If the start of the selection is set first, and if the cursor is after that
character, then the selection is implicitly extended forward to (but not
including) the character where the cursor is. This, for example, provides an
easy way to delete or replace several characters that have just been typed.

If the end of the selection is set first, and if the cursor is before that
character, then the selection is implicitly extended back to (and including)
the character where the cursor is. This, for example, provides an easy way to
replace a word, line, paragraph, etc.

Seek Bars
---------

A ``SeekBar`` is a slider-type control that intuitively (from a visual 
perspective) represents an amount (distance, magnitude, etc). It's often 
used, for example, to represent a volume control. It's normally set by 
tapping the desired point along it. This, of course, can't be done on a 
device that doesn't have a touch screen. Instead, use the Left and Right 
keys (on the D-Pad) to adjust a ``SeekBar``. The Left key decreases its 
setting, and the Right key increases it.

Each time the bar is adjusted, a brief message is displayed that 
announces its new position (as a percentage). Unfortunately, due to a 
current system limitation, its current position can't be determined. 
It's necessary, therefore, to adjust its position back and forth in 
order to figure out what it is. Attempting to increase the bar's value 
when it's already at its maximum, or attempting to decrease its value 
when it's already at its minimum, neither displays a position message 
nor indicates an error.

Legacy Navigation
-----------------

Navigation used to be done by using real keys on an actual keyboard. This
method can still be used via an external keyboard connected via Bluetooth or
USB. The User Interface also supports it by emulating the following keys:

.. include:: tbl-keys-keyboard.rst

What each of these keys does is defined by Android - not by the User Interface.
Here's a summary.

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
  used, because the characters may not all have the same width.

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

Perform a scroll backward (up or left) operation.

* If within a list then move up several elements. If the first element is
  already visible then move to it.

* If within an input area then move up several lines. If the first line is
  already visible then move to it.

* If within a set of pages then move to the previous page.

The Page Down Key
~~~~~~~~~~~~~~~~~

**Space+Dots56**

Perform a scroll forward (down or right) operation.

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


The B2G User Interface
======================

.. contents::

Usage Notes
-----------

Key Combinations
~~~~~~~~~~~~~~~~

A key combination may include any number of keys (including just one). 
There is only one restriction on which keys may be used to form a key 
combination, namely that only one cursor routing key may be used at a 
time. If more than one cursor routing key is pressed at the same time, 
the first one that was pressed is used.

Two actions (a **primary** and a **secondary**) may be assigned to each 
key combination. If any of the keys is released before the long press 
timeout (which is half a second) then the primary action is executed. If 
the key combination is held for the long press timeout, then, even 
without any of the keys being released, the secondary action is 
immediately executed - if a secondary action hasn't been assigned then 
the primary action is executed.

Braille
~~~~~~~

The special character rendered as dots 3678 is used to represent a 
character that doesn't have its own defined representation.

When on an editable text field: Dot 8 is used to show where the cursor 
is, and dots 78 are used to show which characters have been selected. 
Note that the cursor isn't shown when at least one character has been 
selected.

A **checkbox** is rendered as either a space (meaning unchecked) or an 
``X`` (meaning checked) enclosed within [square] brackets, followed by 
its label. For example::

  [ ] This box is not checked.
  [X] This box is checked.

A **switch** is rendered as a **checkbox**. The box is checked if the 
switch is in the **on** position, and unchecked if the switch is in the 
**off** position. For example::

  [ ] Off
  [X] On

If meaningful text for a significant screen element cannot be found then 
it is rendered as its widget type enclosed within (parentheses). For 
example::

  (FrameLayout)
  (ImageView)
  (ListView)

Navigation
----------

Basic Screen Navigation
~~~~~~~~~~~~~~~~~~~~~~~

While the Left, Right, Up, and Down keys (of the D-Pad) can be used to 
navigate the screen, they don't actually work very well. The reason they 
don't is that they try to be too accurate. If the next screen element in 
the requested direction isn't close enough to being exactly in that 
direction then it doesn't get found.

The best way to navigate the screen is to use the Forward and Backward 
keys because they move sequentially through the screen elements without 
missing any of them. The Forward key stops at the end of the screen, and 
the Backward key stops at the beginning of the screen. While these 
(Forward and Backward) keys, when used on their own, perform more 
refined navigation within certain contexts (see below), long pressing 
them can always be used to force an immediate, direct move to the start 
of the next or previous screen element.

If the text of a screen element is longer than the braille display 
and/or has more than one line then the Forward key pans to the right, 
wraps to the start of the next line, etc, as needed, so that all of the 
text is presented. When it reaches the end, it then moves to the start 
of the next element. The Backward key moves through such text in the 
reverse direction, and when it reaches the start of the text it then 
moves to the start of the previous element.

These key combinations perform basic system actions:

Space+Dots123456
  The Home button. Go to the app launcher.

Space+z (dots 1356)
  The Back button.

Space+m (dots 134)
  The Menu button. Go to the current app's main choices screen.

Space+n (dots 1345)
  Go to the Notifications screen.

Space+r (dots 1235)
  Go to the Recent Apps screen.

Space+t (dots 2345)
  Display the current date and time in the ISO 8601 international format
  (YYYY-MM-DD hh:mm:ss). The displayed time is continually updated.

Space+Dots1478
  Go to the Power Off screen.

Center (on the D-Pad)
  Tap (click) the current screen element.

Space+Center
  Hold (long click) the current screen element.

Dot8+c (dots 148)
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

Editable Text Fields
~~~~~~~~~~~~~~~~~~~~

Dots 1 through 6 are used, as defined by the `North American Braille 
Computer Code`_, to type characters, and the Space key is used to type a 
space. Add dot 7 to a letter to make it uppercase. Add dots 7 and 8 to a 
character to type its control variant (see `ASCII Control Characters`_). 
If no characters have been selected then, when a character is typed, it 
is inserted where the cursor is. If characters have been selected then 
the next typed character replaces them.

Dot 8 is the Enter key. If the field supports more than one line, this 
key ends the current line and starts a new one.

Dot 7 is the Backspace key. It deletes the character to the left of the 
cursor. If characters have been selected then it deletes all of them.

Space+d (dots 145) is the Delete key. It deletes the character where the 
cursor is. If characters have been selected then it deletes all of them.

The Forward and Backward keys navigate through the text as defined 
previously, except that they will not leave the field. They must be long 
pressed in order to force an immediate move to the start of the next or 
previous field.

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

These key combinations perform actions on editable text fields:

Dot8+a (dot 1)
  Select all of the text.

Dot8+x (dots 13468)
  Cut the currently selected text to the clipboard.

Dot8+c (dots 148)
  Copy the currently selected text to the clipboard. If no text is
  selected then all of the text is copied.

Dot8+v (dots 12368)
  Paste the clipboard content into the text being edited. If no
  characters have been selected then the clipboard content is inserted
  where the cursor is. If characters have been selected then the
  clipboard content replaces them.

Legacy Key Combinations
~~~~~~~~~~~~~~~~~~~~~~~

Space+Dot1
  Arrow up. Equivalent to the Up key (on the D-Pad).

Space+Dot4
  Arrow down. Equivalent to the Down key (on the D-Pad).

Space+Dot3
  Arrow left. Equivalent to the Left key (on the D-Pad).

Space+Dot6
  Arrow right. Equivalent to the Right key (on the D-Pad).

Space+Dots45
  Enter a ``tab``. A number of apps use this character for moving
  forward through their control widgets.

Space+Dots12
  Enter a ``shift tab``. A number of apps use this character for moving
  backward through their control widgets.

Space+x (dots 1346)
  Enter a control character (see `ASCII Control Characters`_). This
  key combination is a sticky modifier. The next character typed will
  be translated into its control variant. For example, another way to
  enter a ``tab`` character is to type Space+x followed by the letter ``i``.

Key Combinations for Developers
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

A number of key combinations have been defined for developer use. They 
are disabled by default. An always enabled, but intentionally difficult 
to type by accident, key combination toggles their availability. It is::

  Backward + Forward + g (dots 1245)

The developer key combinations are:

Forward+Dots1237
  Turn off all the log categories.

Forward+Dot1
  Toggle the logging of key press and release events from the keyboard.

Forward+Dot2
  Toggle the logging of actions requested by the user.

Forward+Dot3
  Toggle the logging of internal screen navigation operations.

Forward+Dot7
  Toggle the logging of Android accessibility events.

Backward+Dots56
  Describe the current screen element.

Backward+Dot4
  Go to and describe the parent of the current screen element.

Backward+Dot5
  Go to and describe the previous sibling of the current screen element.

Backward+Dot6
  Go to and describe the next sibling of the current screen element.

Backward+Dot8
  Go to and describe the first child of the current screen element.

Backward+Dots4568
  Write descriptions of all of the screen elements to the log.

Space+Up
  Force the current screen element to be scrolled backward.

Space+Down
  Force the current screen element to be scrolled forward.

Tables
------

North American Braille Computer Code
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. include:: nabcc.rst

ASCII Control Characters
~~~~~~~~~~~~~~~~~~~~~~~~

.. include:: ascii.rst


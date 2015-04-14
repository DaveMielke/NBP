The B2G User Interface
======================

.. |the undefined character| replace:: dots 3, 6, 7, and 8 (⣤)
.. |the cursor indicator| replace:: dot 8 (⢀)
.. |the selection indicator| replace:: dots 7 and 8 (⣀)

.. contents::

Usage Notes
-----------

Physical Description
~~~~~~~~~~~~~~~~~~~~

The eight, rectangular, concave keys (in two groups of four) near the 
back of the top comprise a standard, eight-dot, braille keyboard. The 
four on the left, from left to right, are dots 7, 3, 2, and 1. The four 
on the right, from left to right, are dots 4, 5, 6, and 8.

The square with a small, round button in the middle, in between dots 1 
and 4, is the five-key D-Pad. Its four edges are the Up, Down, Left, and 
Right keys. The button in the middle is the Center key.

The long, rectangular key in front of dot 1, the D-Pad, and dot 4 is the 
Space key (or bar).

The braille display (20 eight-dot cells) is at the front of the top. 
There's a small, round button just behind each cell - these are the 
cursor routing keys. The convex, square button to the left of the cursor 
routing keys is the Backward key, and the convex, square button to their 
right is the Forward key.

The earphone/microphone jack is near the front of the left side.
Standard cell phone earphones can be used.

The video jack is in the middle of the left side. It's for an external 
monitor.

A standard USB-A (or host) port is near the back of the left side. USB 
devices (memory stick, external keyboard, mouse, hub, etc) can be 
connected to it.

The volume controls are the two, small, round buttons near the front of 
the right side. The one toward the front is the Volume Down key, and the 
one toward the back is the Volume Up key.

The Power switch is the slide switch near the back of the right side. 
It's **off** position is toward the front, and its **on** position is 
toward the back.

The SD card port is the long slot in the middle of the back.


A micro USB-B (or device) port is near the left of the back. It's 
primary use is for charging the battery. The battery can be charged from 
a computer's USB port.

The internal microphone is just behind a very small slit in the middle of 
the front.

Two internal speakers (left and right channel) are at the left and
right of the front.

The reset button is hidden within a very small, round hole on the 
bottom. If the device is turned over, front to back, the hole is at 
about the eight o'clock position, very close to the foot nearest the 
Power switch. Something thin, like a paperclip, is needed.

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

The way to cancel a key (or keys) that has been accidentally pressed is 
to press enough additional keys such that an unassigned key combination 
is being attempted. Two key combinations have been formally defined for 
this purpose. One is all eight dots, and the other is all eight dots 
plus Space.

Braille
~~~~~~~

The special character rendered as |the undefined character| is used to
represent a character that doesn't have its own defined representation.

When on an editable text field:

* The location of the cursor is shown by |the cursor indicator|.
* The current text selection is highlighted via |the selection indicator|. 

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

Text Selection
~~~~~~~~~~~~~~

When on an editable text field, any sequence of text within that field 
can be easily **selected**. This is how to do it:

1) If necessary, use the Forward and Backward keys to pan to a place 
   where the first character to be selected can be seen.

2) Hold the Backward key while pressing the cursor routing key on the 
   character that is to start the selection.

3) If necessary, use the Forward and Backward keys to pan to a place 
   where the last character to be selected can be seen.

4) Hold the Forward key while pressing the cursor routing key on the 
   character that is to end the selection.

Using steps 1 and 2, the start of the selection can be changed at any 
time. Likewise, using steps 3 and 4, the end of the selection can be 
changed at any time. In fact, the end of the selection can be set before 
the start of the selection is set, i.e. steps 3 and 4 can be performed 
before steps 1 and 2 have been performed.

The current text selection is highlighted via |the selection indicator|.
Selecting text is useful in at least the following ways:

* Typing a character deletes the selected text, puts the cursor where
  the selected text was, and then inserts the character at that point 
  within the text. This, in other words, is an efficient way to replace 
  old text with new text. Just select the old text and then start typing 
  the new text.

* Pressing any of the delete keys (see `Editable Text Fields`_)
  deletes the selected text. This, in other words, is an efficient way 
  to delete a block of text. Just select it and then delete it.

If the start of the selection is set first, and if the cursor is after 
that character, then the selection is implicitly extended forward to 
(but not including) the character where the cursor is. This, for 
example, provides an easy way to delete or replace several characters 
that have just been typed.

If the end of the selection is set first, and if the cursor is before 
that character, then the selection is implicitly extended back to (and 
including) the character where the cursor is. This, for example, 
provides an easy way to replace a word, line, paragraph, etc.

One Hand Mode
~~~~~~~~~~~~~

One Hand Mode is primarily for those users who only have the use of one 
hand. It's off by default. Pressing Forward+Dot8 enables it, and 
pressing Backward+Dot7 disables it. Both of these key combinations have 
been chosen so that they can be reasonably easily pressed with a single 
hand.

When One Hand Mode is on, each key of a key combination may be pressed 
separately. Pressing Space indicates that all of the keys of the 
combination have been pressed. If Space itself is part of the 
combination then it must be pressed first.

For those whose operable hand is sufficiently functional, pressing more 
than one key at a time is supported. The only exception to this is that 
Space, whether pressed at the start (to include it in the combination) 
or at the end (to indicate that the combination is complete), should 
always be pressed separately.

Switching the power off and then back on automatically turns off One 
Hand Mode. This provides an intuitive way for a user who has 
accidentally turned this mode on and doesn't know how to turn it off to 
easily revert the keyboard to normal operation.

Another (legacy) way to turn One Hand Mode on is to hold dot 8 while 
switching the power on.

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

Space+f (dots 124)
  Find text within the current screen element (see `Find Mode`_).

Space+m (dots 134)
  The Menu button. Go to the current app's main choices screen.

Space+n (dots 1345)
  Go to the Notifications screen.

Space+r (dots 1235)
  Go to the Recent Apps screen.

Space+t (dots 2345)
  Display the current date and time in the ISO 8601 international format
  (YYYY-MM-DD hh:mm:ss). The displayed time is continually updated.

Space+? (dots 1456)
  Go to the currently registered **assist** app. The default is
  ``Google Now``.

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

Find Mode
~~~~~~~~~

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
to type by accident, key combination determines their availability. It is::

  Backward + Forward + g (dots 1245)

A long press of this key combination enables them, and a short press 
disables them. The developer key combinations are:

Forward+Dots1237
  Turn off all of the log categories.

Forward+Dot1
  Turn on the logging of key press and release events from the keyboard.

Forward+Dot2
  Turn on the logging of actions requested by the user.

Forward+Dot3
  Turn on the logging of internal screen navigation operations.

Forward+Dot7
  Turn on the logging of Android accessibility events.

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

Quick Reference
---------------

.. include:: ref-actions.rst

The key combinations within these quick reference tables are represented
as follows:

.. include:: ref-keys.rst

Common Operations
~~~~~~~~~~~~~~~~~

.. include:: ref-common.rst

Editable Text Fields and Prompts
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. include:: ref-edit.rst

Navigating the Host Screen
~~~~~~~~~~~~~~~~~~~~~~~~~~

.. include:: ref-host.rst

Developer Actions
~~~~~~~~~~~~~~~~~

.. include:: ref-developer.rst

Tables
------

North American Braille Computer Code
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. include:: nabcc.rst

ASCII Control Characters
~~~~~~~~~~~~~~~~~~~~~~~~

.. include:: ascii.rst


Navigation
==========

Basic Screen Navigation
-----------------------

While the Left, Right, Up, and Down keys (of the D-Pad) can be used to 
navigate the screen, they don't actually work very well. The reason they 
don't is that they try to be too accurate. If the next screen element in 
the requested direction isn't close enough to being exactly in that 
direction then it doesn't get found.

The best way to navigate the screen is to use the Forward and Backward 
keys because they move sequentially through the screen elements without 
missing any of them. The Forward key stops at the end of the screen, and 
the Backward key stops at the beginning of the screen. While these 
(Forward and Backward) keys, when used on their own, may perform more 
refined navigation within certain contexts (see below), long pressing 
them can always be used to force an immediate, direct move to the start 
of the next or previous screen element. Another way to force a move, 
which is especially applicable when secondary actions have been 
disabled, is to press the Space key together with the Forward or Backward 
key.

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
  Display the current date and time in the |clock standard|
  (|clock format|). The displayed time is continually updated.

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

Editable Text Fields
--------------------

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
-----------------------

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
-------------------------------

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


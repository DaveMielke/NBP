The Clipboard
-------------

The clipboard is a central system resource
that facilitates the easy sharing of text between applications.
Text is first moved (cut, copied, added) to the clipboard,
and then pasted into an input area.
Since pasting text doesn't remove it from the clipboard,
the same text may be pasted any number of times.

The |user interface| uses the three traditional keyboard letters (x, c, v),
in combination with Space and either Dot7 or Dot8,
to manage the clipboard.
The Dot8 combinations perform the traditional actions (cut, copy, paste),
and the Dot7 combinations perform extended actions (clear, add, show).
Here's a summary:

.. include:: keys-clipboard.rst

Show
  Press Space+Dot7+v (dots 1236) to check what's current on the clipboard.
  It's presented within a pop-up (see `Pop-ups`_).

Clear
  Press Space+Dot7+x (dots 1346) to clear (erase what's on) the clipboard.

Copy and Add

  * Press Space+Dot8+c (dots 14) to copy text to the clipboard.
    The current clipboard content is replaced.

  * Press Space+Dot7+c (dots 14) to add text to the clipboard.
    The new text is appended to (added to the end of)
    the text that's already on the clipboard.

  If this is done when not within an input area,
  or when within an input area with no selected text,
  then all of the text is copied (or added).
  If this is done within an input area with selected text
  then just that text is copied (or added).

  To copy (or add) part of the text to the clipboard
  when not within an input area:

  1) Move to a place where the first character is visible.

  2) Hold Backward and press the cursor routing key behind the first character.

  3) Move to a place where the last character is visible.

  4) Hold Forward and press the cursor routing key behind the last character.
     The text from the first character through the last character (inclusive)
     will be copied (or added) to the clipboard.
     This step will fail if:

     * The first character hasn't been set.
     * The last character is before the first character.

  Note that these same steps work somewhat differently within an input area
  (see `Text Selection`_).

Cut
  Press Space+Dot8+x (dots 1346) to cut text to the clipboard.
  This can only be done within an input area.
  The text is removed from the input area
  and replaces the current clipboard content.

  * |if cursor| all of the text is cut.
  * |if selection| just that text is cut.

Paste
  Press Space+Dot8+v (dots 1236) to paste
  the text that's currently on the clipboard into an input area.

  * |if cursor| the clipboard content is inserted just to the left of the cursor.
  * |if selection| the clipboard content replaces the selected text.


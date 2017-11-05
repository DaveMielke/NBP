.. |move cursor to start of selection| replace::
  move the cursor to the first selected character
  and then clear the selection

.. |move cursor to end of selection| replace::
  move the cursor to the character just after it
  and then clear the selection

.. |describe vertical cursor motion within text| replace::
  Vertical cursor motion within text may cause it
  to also move unexpectedly left or right in braille,
  especially when a proportional font has been used,
  because the characters on the screen may not all have the same width.

Space + Dot3
````````````

* |if cursor| move the cursor one character to the left.
  If it's at the start of a line
  then wrap it to the end of the previous line.

* |if selection| |move cursor to start of selection|.

Space + Dot6
````````````

* |if cursor| move the cursor one character to the right.
  If it's at the end of a line
  then wrap it to the start of the next line.

* |if selection| |move cursor to end of selection|.

Space + Dots36
``````````````

* |if cursor| speak the current character.

* |if selection| speak each of the selected characters.

Long Press of Space + Dots36
````````````````````````````

* |if cursor| speak the current character phonetically.

* |if selection| speak each of the selected characters phonetically.

|need long key presses|

Space + Dot2
````````````

Move the cursor to the start of the current word.
If it's at the start of a word
then move it to the start of the previous word.

Space + Dot5
````````````

Move the cursor to the start of the next word.

Space + Dots25
``````````````

* |if cursor| speak the current word.

* |if selection| speak the selected text.

Long Press of Space + Dots25
````````````````````````````

* |if cursor| spell the current word.

* |if selection| spell the selected text.

|need long key presses|

Space + Dot1
````````````

* |if cursor| move the cursor one line up.
  |describe vertical cursor motion within text|

* |if selection| |move cursor to start of selection|.

Space + Dot4
````````````

* |if cursor| move the cursor one line down.
  |describe vertical cursor motion within text|

* |if selection| |move cursor to end of selection|.

Space + Dots23
``````````````

Move the cursor to the start of the first line of the current paragraph.
If it's on the first line of a paragraph
then move it to the start of the first line of the previous paragraph.

Space + Dots56
``````````````

Move the cursor to the start of the first line of the next paragraph.

Space + Dots13
``````````````

Move the cursor to the start of the current line.

Space + Dots46
``````````````

Move the cursor to the end of the current line.

Space + Dots123
```````````````

Move the cursor to the start of the first line.

Space + Dots456
```````````````

Move the cursor to the end of the last line.


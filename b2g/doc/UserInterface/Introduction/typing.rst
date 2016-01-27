Typing Characters
-----------------

Input Modes
~~~~~~~~~~~

Space+i (dots 24) displays a message (see `Messages`_) that confirms
which input mode is currently being used.

Text mode is the default, and is used for `Directly Typing Regular Text`_.

Braille mode is used for `Directly Typing Actual Braille Cells`_.

Either booting the device or waking it up (see `The Power Switch`_)
automatically resets the input mode to Text.

Directly Typing Regular Text
````````````````````````````

Space+Dot7+i (dots 24) restores the input mode to Text (the default).
This mode honours the currently selected braille mode
(see `Braille Modes and Codes`_).

Typing Literary Braille Characters
''''''''''''''''''''''''''''''''''

Any character (or character sequence) that has a defined `Literary Braille`_
representation within the currently selected braille code can be typed.

In general, a cursor routing key corresponds to the first character of the
literary braille symbol that it's behind. The only exception to this rule is
when setting the end of a text selection (see `Selecting Text`_). In that case
it corresponds to the last character of the symbol.

A cursor routing key may, on occasion, seem to be corresponding to the wrong
character. This is because the rules used to define the currently selected
braille code sometimes need to group more than one symbol together. For
example, in UEB (Unified English Braille), at the time of this writing:

* There are two ways to contract "ear": either by using the "ea" contraction or
  by using the "ar" contraction. To ensure that the "ar" contraction is always
  used, the whole sequence is covered by a single rule. The cursor routing key
  behind the "ar" contraction, therefore, unexpectedly corresponds to the "e"
  rather than to the "a".

* The honourific title "Saint" is often written as "St.". In order to prevent
  the "st" contraction from being used, there's a rule that also covers the
  trailing period. When setting the end of a text selection, therefore, the
  cursor routing keys behind the "s" and the "t" unexpectedly set the end of
  the selection to the period.

Deleting characters (see `Input Areas`_) is done one text character at a time.

A special literary braille prefix, e.g. a capitalization sign, may not appear
until the first character of the symbol it applies to has been typed.

`Indirectly Typing Any Character`_ doesn't work when literary
braille is being used. If you need to do it then you'll need to (temporarily)
switch to `Computer Braille`_ mode.

Typing Computer Braille Characters
''''''''''''''''''''''''''''''''''

Any character that has a defined `Computer Braille`_ representation
(see `Computer Braille Characters`_) can be directly typed by pressing the
corresponding keys as a single combination. There are two exceptions to this
simple (and obvious) rule.

If the defined representation of a character is either just dot 7 or just dot 8
then it can't be typed by pressing the corresponding key because those keys
are, respectively, Backspace and Enter. Press Space+Dot7 for just dot 7, and
press Space+Dot8 for just dot 8.

Directly Typing Actual Braille Cells
````````````````````````````````````

Space+Dot8+i (dots 24) sets the input mode to Braille.

In this input mode, the characters that render in text as actual braille cells
(see `Unicode Braille Patterns`_) can be directly typed by pressing the
corresponding keys as a single combination.

Two of these characters can't be typed by pressing the corresponding keys -
just dot 7 (because it's the Backspace key), and just dot 8 (because it's the
Enter key). Press Space+Dot7 for just dot 7, and press Space+Dot8 for just dot
8.

These characters have several uses, including (but not limited to):

* Actual braille characters can be written into text documents.

* Contracted braille can be accurately saved.

* Braille music can be accurately saved, and also shared with others who use
  different localized braille character mappings.

Typing a Control Character
~~~~~~~~~~~~~~~~~~~~~~~~~~

In order to type a control character, press Space+x (dots 1346) immediately
before the letter or special symbol that represents it (see `ASCII Control
Characters`_). For example, in order to type a tab (which happens to be control
I), press Space+x and then immediately type the letter ``i``.

The letter or special symbol must be typed within
|intermediate action timeout|.

Indirectly Typing Any Character
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Any character can always be indirectly typed, even if directly typing it isn't 
possible:

* Its braille representation hasn't been defined.

* You don't know its braille representation.

* It's represented in braille by either just dot 7 (the Backspace key) or just
  dot 8 (the Enter key).

In order to indirectly type it, press Space+u (dots 136). This brings up a
prompt (see `Prompts`_) with the following header::

  Unicode> U+

``U+`` is the conventional prefix for the hexadecimal value of a Unicode 
character. The prompt allows you to enter up to four hexadecimal digits -
``0`` through ``9`` and ``A`` through ``F`` (either upper or lower case).
Leading zeroes may be omitted. The Unicode values of characters are, of course,
beyond the scope of this document.

The digits you enter may be freely edited, e.g. the cursor can be moved, any
digit may be deleted, a new digit may be inserted, etc. In other words, making
corrections doesn't require backspacing and retyping. When you're done, press
Enter.

If no digits have been entered yet (or if they've all been deleted) then you'll
only see the header. If, however, at least one digit has been entered then the
character represented (so far) by the digit(s) is displayed just to the right,
and that character's formal name is displayed on the next line.

To illustrate, let's see how a lowercase ``s`` would be indirectly typed. It's
Unicode value is U+0073. In order to keep this example simple, let's skip the
two leading zeroes.

.. topic:: Indirectly Typing the Letter ``s`` (U+0073)

  1) Press Space+u (dots 136)::

       Unicode> U+

  2) Type the digit ``7`` (dots 2356)::

       Unicode> U+7 ⣛
       bell

  3) Type the digit ``3`` (dots 25)::

       Unicode> U+73 ⠎
       latin small letter s

  4) Press Enter. The prompt goes away, and the ``s`` is typed.


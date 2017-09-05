Speech
------

Speech is supported, and is enabled by default.
Pressing Forward+VolumeUp enables it,
and pressing Forward+VolumeDown disables it.
Alternate combinations that are usable in `One Hand Mode`_,
which use Dot6 instead of Forward,
have been defined.
Speech can't be disabled while braille is disabled
(see `Disabling the Display`_).

* To interrupt what's currently being spoken, press Dot1+Left.

* To hear all of the current line, press Dot1+Right.
  Another (legacy) way to do so is to press Space+c (dots 14).

* To hear all of the current screen element, press Dot1+Center.

* To hear just what's before the start of the braille display,
  press Dot1+Up.

* To hear just what's on and after the braille display,
  press Dot1+Down.

Adjusting the Volume:
  * Press Dot1+VolumeDown for **softer**.
  * Press Dot1+VolumeUp for **louder**.

  The speech volume is set relative to the system volume. If, therefore, the
  speech volume is set as high as it'll go but still sounds too soft then try
  increasing the system volume (by pressing VolumeUp by itself).

Adjusting the Rate:
  * Press Dot2+VolumeDown for **slower**.
  * Press Dot2+VolumeUp for **faster**.

Adjusting the Pitch:
  * Press Dot3+VolumeDown for **lower**.
  * Press Dot3+VolumeUp for **higher**.

Adjusting the Balance:
  * Press Dot4+VolumeDown for **more left**.
  * Press Dot4+VolumeUp for **more right**.

Customizing What is Spoken
~~~~~~~~~~~~~~~~~~~~~~~~~~

There are a number of `speech settings`_ that can be used
to customize how much is spoken.
All of them default to ``on``,
but each of them can be individually turned ``off``.
They are:

Echo Words
  Speak each word after it's been fully typed.
  A word is deemed to be complete as soon as a space is typed.

Echo Characters
  Speak each character as it's typed.
  If characters are typed quickly,
  e.g. when pasting from `the clipboard`_,
  then they may be grouped together and
  pronounced, for example, as a partial word.

Echo Deletions
  Speak each character as it's deleted (see `Input Areas`_).
  The character being deleted is spoken,
  followed by the word ``deleted``.
  If characters are deleted quickly,
  e.g. when deleting a block of selected text (see `Text Selection`_),
  then they may be grouped together and
  pronounced, for example, as a partial word.

Echo Selection
  Speak changes to the start or end of the `text selection`_.
  The character at the new position is spoken,
  followed by the phrase ``start of selection`` or ``end of selection``.

Speak Lines
  Speak the entire line
  either when vertical navigation explicitly switches to a new one
  or when horizontal navigation implicitly wraps to a new one.

Sleep Talk Mode
~~~~~~~~~~~~~~~

Sleep Talk Mode leaves speech active while the Power switch is off. This
capability is useful when, for example, you'd like to reduce battery drain but
still be informed when an important asynchronous event, e.g. the arrival of a
notification, occurs.

This mode is disabled by default. Pressing Backward+VolumeUp enables it, and
pressing Backward+VolumeDown disables it.
Alternate combinations that are usable in `One Hand Mode`_,
which use Dot5 instead of Backward,
have been defined.

Differences from Braille Rendering
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Speech is rendered differently than braille is in the following ways:

* The [brackets] around screen element descriptions provided by application
  developers aren't spoken.

* The {braces} around screen element types aren't spoken.

* A space is inserted in between each pair of a lowercase letter followed by an
  uppercase letter within screen element types in order to improve the way that
  each implied word is pronounced. For example, ``SeekBar`` is spoken as
  ``Seek Bar``.

* The (parentheses) around screen element states aren't spoken.

* The state of a checkbox (or switch) is spoken as either ``checked`` or
  ``not checked``.


Special Features
----------------

Braille
~~~~~~~

The On-screen Monitor
`````````````````````

An on-screen monitor that shows what's on the braille display can be enabled
from `The Settings Screen`_. It shows both the braille cells and the text.

Adjusting the Dot Firmness
``````````````````````````

The firmness of the braille dots can be adjusted from `The Settings Screen`_.

Disabling the Display
`````````````````````

The braille display is enabled by default. It can be disabled by pressing
Backward+Dot1, and reenabled by pressing Forward+Dot4.
Either booting the |product name| or waking it up (see `The Power Switch`_)
also reenables it if `speech`_ is disabled.
Braille can't be disabled while `speech`_ is disabled.

Speech
~~~~~~

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

Sleep Talk Mode
```````````````

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
``````````````````````````````````

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


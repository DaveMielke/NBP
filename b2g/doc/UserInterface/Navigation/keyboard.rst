Navigating with Keyboard Keys
-----------------------------

Navigation used to be done by using real keys on an actual keyboard. This
method can still be used via an external keyboard connected via Bluetooth or
USB. The |user interface| also supports it by emulating the following keyboard
keys:

.. include:: keys-keyboard.rst

What each of these keys does is defined by Android - not by the
|user interface|. Because of this, navigating with these keys isn't fully
compatible with |user interface| navigation:

* `Navigating with the Forward and Backward Keys`_
* `Navigating with the Directional Keys`_

Android keeps track of the current screen element when these keys are used, and
|user interface| navigation can't override this. So, for each screen:

* The first keyboard navigation operation is relative to the current screen
  element.

* Any subsequent keyboard navigation operation is relative to the result of the
  previous keyboard navigation operation. Intervening |user interface|
  navigation operations won't be considered.

* |user interface| navigation operations are always relative to the current
  screen element no matter which type of navigation was previously used.

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

* If within an input area then move up to the first line of the current
  paragraph. If already on the first line of a paragraph then move up to the
  first line of the previous paragraph.

* If within a set of pages then move to the previous page.

The Page Down Key
~~~~~~~~~~~~~~~~~

**Space+Dots56**

Perform a scroll forward (down or to the right) operation.

* If within a list then move down several elements. If the last element is
  already visible then move to it.

* If within an input area then move down to the first line of the next
  paragraph.

* If within a set of pages then move to the next page.

The Home Key
~~~~~~~~~~~~

**Space+Dots13**

Move to the first element of a list, to the start of a line of text, to the
first page of a group, etc.

The End Key
~~~~~~~~~~~

**Space+Dots46**

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


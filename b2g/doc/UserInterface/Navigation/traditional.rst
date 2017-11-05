Navigating with Traditional Key Combinations
--------------------------------------------

The |user interface| supports screen navigation via key combinations
|traditional key combinations|.
See `Traditional Key Combinations within an Input Area`_
for how they work within an input area.

Physical Keyboard Key Emulation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

What each of these combinations does is defined by Android -
not by the |user interface| -
so navigating with them is, unfortunately,
not fully compatible with |user interface| navigation:

* `Navigating with the Panning Keys`_
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

.. |cursor navigation is unreliable| replace::
  This operation is not only imprecise, but also doesn't find screen
  elements that merely present helpful text.

The Enter Key
`````````````

**Dot8**

* |if button| press it.

* |if checkbox| check/uncheck it.

* |if switch| move it to its other position.

* |if folder| open it.

* |if app| go to it.

The Cursor Left Key
```````````````````

**Space + Dot3**

* Move to the nearest screen element that's roughly to the left of the
  current one.
  |cursor navigation is unreliable|

The Cursor Right Key
````````````````````

**Space + Dot6**

* Move to the nearest screen element that's roughly to the right of the
  current one.
  |cursor navigation is unreliable|

The Cursor Up Key
`````````````````

**Space + Dot1**

* Move to the nearest screen element that's roughly above the current one.
  |cursor navigation is unreliable|

The Cursor Down Key
```````````````````

**Space + Dot4**

* Move to the nearest screen element that's roughly below the current one.
  |cursor navigation is unreliable|

The Page Up Key
```````````````

**Space + Dots23**

Perform a scroll backward (up or to the left) operation.

* |if list| move up several elements. If the first element is
  already visible then move to it.

* |if pages| switch to the previous one.

The Page Down Key
`````````````````

**Space + Dots56**

Perform a scroll forward (down or to the right) operation.

* |if list| move down several elements. If the last element is
  already visible then move to it.

* |if pages| switch to the next one.

The Home Key
````````````

**Space + Dots13**

* |if list| move to its first element.

* |if pages| switch to the first one.

The End Key
```````````

**Space + Dots46**

* |if list| move to its last element.

* |if pages| switch to the last one.

The Control+Home Keys
`````````````````````

**Space + Dots123**

Move to the start of the first line of text.

The Control+End Keys
````````````````````

**Space + Dots456**

Move to the end of the last line of text.

The Shift+Tab Keys
``````````````````

**Space + Dots12**

Move to the previous screen element that can perform an action.
If on the first screen element then wrap to the last one.

The Tab Key
```````````

**Space + Dots45**

Move to the next screen element that can perform an action.
If on the last screen element then wrap to the first one.

Other Traditional Key Combinations
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Space + Dots126
```````````````

Move directly to the first screen element (usually at the top-left).

Space + Dots345
```````````````

Move directly to the last screen element (usually at the bottom-right).


Key Combinations
----------------

A key combination may include any number of keys (including just one). 
There's only one restriction regarding which keys may be used to form a key 
combination, namely that only one cursor routing key may be used at a 
time. If more than one cursor routing key is pressed at the same time, then
the first one that was pressed is used.

If any of the keys of a combination is released before the long press timeout
(see `Long Press Mode`_) expires then the action bound to that combination is
executed. If the timeout expires then the action is immediately executed
even though none of the keys has been released yet.

The way to cancel a key combination that's been accidentally started is to
press enough additional keys such that an unbound key combination is being
attempted. A key combination that's been formally defined for this purpose is
all eight dots plus Space.

Long Press Mode
~~~~~~~~~~~~~~~

A secondary action may be bound to a key combination. A key combination's
secondary action is executed if all of its keys are held until the long press
timeout expires. The long press timeout is |long press timeout|.

This mode is enabled by default. Pressing Forward+Dot6 enables it,
and pressing Backward+Dot3 disables it.

One Hand Mode
~~~~~~~~~~~~~

One Hand Mode is primarily for those users who only have the use of one hand.
When it's on, each dot key of a combination may be pressed separately.
For those whose operable hand is sufficiently functional,
pressing more than one key at a time is supported.
Pressing Space indicates that all of the keys have now been pressed.
If Space itself is part of the combination
then it must be additionally pressed first.
So:

1) If Space is part of the combination then press it first.
2) Press the dot keys of the combination either separately or in groups.
3) Press Space to indicate that the combination has been fully entered.

To type a space (which is a key combination without any dots),
simply press Space twice (with no dots in between).
There's an efficient way to add a space immediately after typing a character
(except after a space).
After pressing all of the dots that represent the character,
followed by Space to cause it to be typed,
quickly press Space again (just once).
The amount of time that you have to do this
(before pressing Space begins a new key combination)
can be customized via the Space Timeout setting.

Pressing any of the other keys:

* a panning key (Forward, Backward)
* a D-Pad key (Up, Down, Left, Right, Center)
* a cursor routing key
* a volume key

also signals the completion of a combination.
This means that a combination involving more than one of these other keys
cannot be used when in One Hand Mode.
Additional alternate combinations have been defined, therefore,
in order to mitigate this restriction.
They are:

.. include:: keys-onehand.rst

One Hand Mode is disabled by default.
Pressing Dot8+Forward enables it,
and pressing Dot7+Backward disables it.
When disabling it, Dot7 must, of course, be pressed first.
Both of these key combinations have been chosen so that
they can be reasonably easily pressed with a single hand.


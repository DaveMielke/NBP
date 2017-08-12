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

One Hand Mode has been designed to make the |product name| easy to use
by those who only have the use of one of their hands.
It's disabled by default.
Press Dot8+Forward to enable it,
and press Dot7+Backward to disable it.
Both of these key combinations have been chosen so that
they can be reasonably easily pressed with a single hand.

When One Hand Mode is enabled,
each dot key of a combination may be pressed separately.
For those whose operable hand is sufficiently functional,
pressing more than one key at a time is supported.
Press Space to signal that the combination is complete,
i.e. that all of its keys have now been pressed.
If the combination contains the Space key
then an additional Space must be pressed first.
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

If Space is pressed at the same time as at least one other key
then all of the pressed keys (including Space)
are added to the current combination,
and the combination is then considered to be complete.
This allows users who are able to press Space together with other keys
to navigate more quickly.

Pressing any of the other |product name| keys:

* a panning key (Forward, Backward)
* a D-Pad key (Up, Down, Left, Right, Center)
* a cursor routing key
* a volume key

also signals the completion of the current combination.
This means that you don't then press Space to indicate that you're done.
It also means that a combination involving more than one of these other keys
can't be used when in One Hand Mode.
Additional alternate combinations have been defined, therefore,
in order to mitigate this restriction.
They are:

.. include:: keys-onehand.rst


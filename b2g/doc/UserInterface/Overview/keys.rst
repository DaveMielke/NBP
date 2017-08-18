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
Pressing Dot8+Forward enables it,
and pressing Dot7+Backward disables it.
Both of these key combinations were chosen so that
they can be reasonably easily pressed with a single hand.

When One Hand Mode is enabled,
each dot key of a combination may be pressed separately.
For those whose operable hand is sufficiently functional,
pressing more than one key at a time is supported.
Press Space after all of the keys have been pressed
to indicate that the combination has been fully entered.
The Space key itself isn't included within the combination.
If the combination does contain the Space key
then an additional Space must be pressed, by itself, first.

So:

1) If Space is part of the combination then press it, on its own, first.
2) Press the dot keys of the combination either separately or in groups.
3) Press Space to indicate that the combination has been fully entered.

To type a space (which is a key combination without any dots),
simply press Space twice (with no dot keys in between).
There's a quick way to type a space immediately after typing a character
(except after a space).
After pressing all of the dots that represent the character,
followed by Space to complete that combination,
quickly press Space again (just once).
The amount of time that you have to do this
(before pressing Space reverts to starting a new combination that includes it)
can be customized via the Space Timeout setting (which defaults to 1 second).

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

also signals the completion of the current key combination.
In this case, you don't press Space to indicate that you're done.
That key, as well as all of the other keys
that are also pressed at the same time,
are included within the current combination.

While this feature allows for natural and efficient screen navigation,
it also means that a combination involving more than one of these other keys
can't be used when in One Hand Mode.
Additional alternate combinations have been defined, therefore,
in order to mitigate this restriction.
They are:

.. include:: keys-onehand.rst

If you don't press any keys for a while after having started a combination
then that combination is automatically cancelled and you need to start over.
The amount of time before a combination is cancelled can be customized
via the Pressed Timeout setting (which defaults to 15 seconds).


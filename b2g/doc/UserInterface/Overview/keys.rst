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

One Hand Mode is primarily for those users who only have the use of one 
hand. When it's on, each key of a key combination may be pressed separately.
Pressing Space indicates that all of the keys of the combination have been
pressed. If Space itself is part of the combination then it must be pressed
first.

For those whose operable hand is sufficiently functional, pressing more 
than one key at a time is supported. The only exception to this is that 
Space, whether pressed at the start (to include it in the combination) 
or at the end (to indicate that the combination is complete), should 
always be pressed separately.

This mode is disabled by default. Pressing Forward+Dot8 enables it, and
pressing Backward+Dot7 (followed, of course, by pressing Space) disables it.
Both of these key combinations have been chosen so that they can be reasonably
easily pressed with a single hand.

Switching the Power switch **off** and then back **on** automatically disables
One Hand Mode. This provides an intuitive way for a user who has accidentally
enabled this mode, and who may not know how to disable it, to easily revert the
keyboard to normal operation.

Another (legacy) way to enable One Hand Mode is to hold Dot8 while
switching the power on.


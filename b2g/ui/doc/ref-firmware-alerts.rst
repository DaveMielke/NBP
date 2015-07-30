A high-pitched beep (|beep frequency|) is used to alert the user to a number of
firmware-detected states.

.. table:: Beep Lengths

   ======  ====================
   Length  Duration
   ------  --------------------
   Short   |short beep length|
   Medium  |medium beep length|
   Long    |long beep length|
   ======  ====================

One Short Beep
  The system has started booting. This occurs immediately after the Power
  switch has been turned on while the system is shut down. The braille display
  is not powered up until later.

Two Short Beeps
  The kernel has finished initializing. This occurs part way through the
  system's boot sequence. The braille display is powered up at this point.
  It will say ``Starting`` for a few seconds, and then go blank. Eventually,
  when the User Interface starts, regular screen content will appear.

Three Short Beeps
  The battery is very low.

Two Medium Beeps
  The battery is full. This occurs when the charger is connected, and indicates
  that further charging is unnecessary.

One Long Beep
  The device has been successfully reset (see `The Reset Button`_).


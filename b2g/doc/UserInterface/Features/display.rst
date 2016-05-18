The Remote Braille Display
--------------------------

The |product name| can be used as a remote braille display for
a desktop,
a laptop,
another mobile device,
etc.
A screen reader that supports braille devices
must be running on the other system.

Configuration of the screen reader that's running on the other system
is beyond the scope of this manual.
Here, however, are some things you may need to know when configuring it:

* The remote braille display communicates with the screen reader via Bluetooth.
  The |product name| will probably need to be paired with the other system
  before the screen reader can use it.

* The remote braille display emulates the Baum protocol.
  Other names that the screen reader may use for this protocol
  include (but aren't limited to):
  Vario, RBT, Braille Connect, Conny

* The remote braille display identifies itself to the screen reader
  as being the Conny model
  in order to hint as to which keys it can emulate.

Relevant Settings
~~~~~~~~~~~~~~~~~

The following settings (on `The Settings Screen`_)
pertain to the remote braille display:

Braille Display
  A checkbox that enables (checked) and disables (unchecked)
  the remote braille display.
  It's disabled by default.

Secure Connection
  A checkbox that enables (checked) and disables (unchecked)
  secure Bluetooth connection capabilities.
  It's disabled by default.
  When enabled, the connection is guaranteed to be authenticated and encrypted.
  Some systems, Bluetooth adapters, etc (especially older ones)
  don't support these capabilities.
  You may need to try both settings.

Important Key Combinations
~~~~~~~~~~~~~~~~~~~~~~~~~~

Pressing Space+Dots78 switches to the remote braille display.
It'll contain one of the following messages
when not connected to a screen reader:

braille display off
  The remote braille display hasn't been enabled.

Bluetooth off
  The remote braille display has been enabled
  but Bluetooth hasn't been turned on.

Bluetooth waiting
  The remote braille display is waiting
  for a screen reader on another system
  to connect to it via Bluetooth.

When the remote braille display is connected,
the screen reader owns all of the keys on top of the |product name|.
The only keys that the |user interface| still owns
are the two volume keys and the power switch.
Pressing the volume keys separately still controls
the level of Android's current audio stream.
Pressing both of them together
returns full control to the |user interface|.

Baum Key Emulation
~~~~~~~~~~~~~~~~~~

The Baum device that the remote braille display emulates
has the following keys:

* A Cursor routing key immediately behind each of the braille cells.

* Three Display keys at each end of the braille cells.
  The three on the left, from top to bottom, are D1, D2, and D3.
  The three on the right, from top to bottom, are D4, D5, and D6.

* An eight-dot braille keyboard is behind the cursor routing keys.
  The four on the left, from left to right, are dots 7, 3, 2, and 1.
  The four on the right, from left to right, are dots 4, 5, 6, and 8.
  These keys are also known as B1 through B8
  (numbered the same way as the dots).

* A five-position joystick in the middle of the front.
  Its positions are Left, Right, Up, Down, and Press.

* A button on each side of the joystick.
  The one on the left is B9, and the one on the right is B10.

* Two concave buttons at each end of the front.
  The two on the left, from left to right, are F1 and F2.
  The two on the right, from left to right, are F3 and F4.

* A button in between dots 1 and 4 named B11.

The |product name|'s cursor routing keys are always mapped
to the Baum cursor routing keys.

The |product name|'s five-key directional pad is always mapped
to the Baum five-position joystick.

The |product name|'s Dot keys, Backward, Forward, and Space are mapped
according to one of the following operational modes:

Baum Navigation Mode
````````````````````

Pressing Space while holding Backward switches to navigation mode.
This is the default.
In it, the keys are mapped as follows:

* Dots 1 through 6 are mapped to Display keys 1 through 6.

* Dots 7 and 8 are mapped to Function keys 3 and 4.

* Backward and Forward are mapped to Function keys 1 and 4.

* Space isn't mapped.

Baum Keyboard Mode
``````````````````

Pressing Space while holding Forward switches to keyboard mode.
In it, the keys are mapped as follows:

* Dots 1 through 8 are mapped to Dots 1 through 8
  (also known as B1 through B8).

* Backward and Forward are mapped to B9 and B10.

* Space is mapped to B11.


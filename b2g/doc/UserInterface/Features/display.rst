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

The Baum protocol supports more keys than the |product name| has,
so the following operational modes are supported:

* Pressing Space while holding Backward switches to `Navigation Mode`_.
  This is the default.

* Pressing Space while holding Forward switches to `Keyboard Mode`_.

Navigation Mode
```````````````

==================  ========  =============
|product name| Key  Baum Key  Also Known As
------------------  --------  -------------
Dot1                D1        Display1
Dot2                D2        Display2
Dot3                D3        Display3
Dot4                D4        Display4
Dot5                D5        Display5
Dot6                D6        Display6
Backward            F1        Function1
Dot7                F2        Function2
Dot8                F3        Function3
Forward             F4        Function4
==================  ========  =============

Keyboard Mode
`````````````

==================  ========  =============
|product name| Key  Baum Key  Also Known As
------------------  --------  -------------
Dot1                B1        Dot1
Dot2                B2        Dot2
Dot3                B3        Dot3
Dot4                B4        Dot4
Dot5                B5        Dot5
Dot6                B6        Dot6
Dot7                B7        Dot7
Dot8                B8        Dot8
Backward            B9
Space               B11
Forward             B10
==================  ========  =============


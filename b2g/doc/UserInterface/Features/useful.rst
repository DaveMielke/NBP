Useful Features
---------------

Checking the Time
~~~~~~~~~~~~~~~~~

You can access a braille-friendly clock by pressing Space+t (dots 2345).
The date and time are displayed on the first line using the same format
currently configured for the Android clock except that it also includes seconds.
The second line contains the abbreviated week day and month names,
and the third line identifies the time zone.
For example::

  2012/02/29 10:26:53
  Wed, Feb 29
  EST (UTC-0500)

The four-digit number within (parentheses) on the third line
is the time zone's offset, in (two-digit) hours and (two-digit) minutes,
from UTC (Universal Coordinated Time).
The abbreviation EST (in this example) means Eastern Standard Time
(a North American time zone),
which is five hours earlier (hence the minus sign [-]) than UTC.

Checking Status Indicators
~~~~~~~~~~~~~~~~~~~~~~~~~~

You can check various helpful status indicators by pressing Backward and
Forward together. This will cause the indicators to be presented within a popup
(see `Popups`_) that looks something like this::

  Alert: Airplane Mode
  Battery: 78% charging USB
  SIM: MyProvider 75% GSM idle EDGE connected AccessPointName
  Wi-Fi: MyNetwork 60% 54Mbps
  Bluetooth: on discovering discoverable connectable

Identifying the Build
~~~~~~~~~~~~~~~~~~~~~

There may be times when you need to identify the exact build of the
|user interface| that you're currently using. For example, this information is
particularly helpful when you're reporting a problem or checking if there's a
newer version.

You can get this information by pressing Space, Backward, and Forward
together. This will cause details that identify the build to be presented
within a screen that looks something like this::

  UI Package Version    1.11.24
  UI Source Revision    git:4076b079460da5e1
  UI Build Time         2016-03-24@01:27 UTC
  Android Version       4.1.2
  Android Build Name    v4.3.7
  Android Build Time    2016-03-21@06:10 UTC
  Android Build Type    eng
  Linux Kernel Version  2.6.37
  Main Firmware         4.0
  Base Firmware         128.0
  Metec Driver Version  1.00

Builtin Shortcuts
~~~~~~~~~~~~~~~~~

Shortcuts to Useful Android Screens
```````````````````````````````````

The following shortcuts to useful Android screens are provided:

Space+p (dots 1234)
  Go to the Power Off screen.

Space+? (dots 1456)
  Go to the currently registered **assist** app. The default is
  ``Google Now``.

Space+s (dots 234)
  Begin a shortcut. If one of the following key combinations is entered within
  |partial entry timeout|
  then the action it's bound to is immediately performed.
  If not, then an `Action Chooser`_ is presented
  that let's you pick from the following choices.

  a (dot 1)
    Go to the Accessibility Settings screen.

  b (dots 12)
    Go to the Bluetooth Settings screen.

  c (dots 14)
    Go to the Calendar app.

  d (dots 145)
    Go to the Dialer (Phone) app.

  e (dots 15)
    Go to the EMail app.

  g (dots 1245)
    Go to the Gallery app.

  m (dots 134)
    Go to the Music app.

  p (dots 1234)
    Go to the People (Contacts) app.

  s (dots 234)
    Go to the Android Settings screen.

  st (dots 34)
    Go to the Store (Market) app.

  t (dots 2345)
    Go to the Text (Messaging) [SMS, MMS] app.

  v (dots 1236)
    Initiate an Android voice command.

  w (dots 2456)
    Go to the Wi-Fi Settings screen.

  # (dots 3456)
    Go to the Calculator app.

Shortcuts to Other |product name| Applications
``````````````````````````````````````````````

The following shortcuts to other |product name| applications are provided:

Space+ed (dots 1246)
  Go to the |product name| editor.

Describing how any of these applications work is, of course, beyond the scope
of this document.


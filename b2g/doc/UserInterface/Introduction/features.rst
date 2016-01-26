Additional Features
-------------------

Reading This Document
~~~~~~~~~~~~~~~~~~~~~

A copy of this document is available in plain text on the |product name|.
Press Space+h (dots 125) to access it.
See `Finding Text within the Current Screen Element`_ for tips on how to find
your way around within it.

Checking the Time
~~~~~~~~~~~~~~~~~

You can access a braille-friendly clock by pressing Space+t (dots 2345). The
date and time are displayed on the first line using the conventional
international format. The second line contains the abbreviated week day and
month names, and the third line identifies the time zone. For example::

  2012-02-29 10:26:53
  Wed, Feb 29
  EST (-0500)

The four-digit number within (parentheses) on the third line is the time zone's
offset, in (two-digit) hours and (two-digit) minutes, from UTC (Universal
Coordinated Time). The abbreviation EST means Eastern Standard Time in North
America, which is five hours earlier (hence the leading minus sign) than UTC.

Checking Status Indicators
~~~~~~~~~~~~~~~~~~~~~~~~~~

You can check various helpful status indicators by pressing Backward and
Forward together. This will cause the indicators to be presented within a popup
(see `Popups`_) that looks something like this::

  Battery: 78% charging USB
  Wi-Fi: MyNetwork 60% 54Mbps

Identifying the Build
~~~~~~~~~~~~~~~~~~~~~

There may be times when you need to identify the exact build of the
|user interface| that you're currently using. For example, this information is
particularly helpful when you're reporting a problem or checking if there's a
newer version.

You can get this information by pressing Space, Backward, and Forward
together. This will cause details that identify the build to be presented
within a screen that looks something like this::

  UI Package Version    1.7.74
  UI Source Revision    git:e6193a401c1a5691
  UI Build Time         2016-01-21@09:40 UTC
  Android Build Name    v4.2.8-beta
  Android Build Time    2015-12-01@03:09 UTC
  Android Build Type    eng
  Linux Kernel Version  2.6.37
  Firmware Version      3.1
  Metec Driver Version  1.00

Shortcuts
~~~~~~~~~

To Useful Android Screens
`````````````````````````

The following shortcuts to useful Android screens are provided:

Space+p (dots 1234)
  Go to the Power Off screen.

Space+? (dots 1456)
  Go to the currently registered **assist** app. The default is
  ``Google Now``.

Space+s (dots 234)
  a (dot 1)
    Go to the Accessibility Settings screen.

  b (dots 12)
    Go to the Bluetooth Settings screen.

  s (dots 234)
    Go to the main Settings screen.

  w (dots 2456)
    Go to the Wi-Fi Settings screen.

To Other |product name| Applications
````````````````````````````````````

The following shortcuts to other |product name| applications are provided:

Space+ed (dots 1246)
  Go to the |product name| editor.

Describing how any of these applications work is, of course, beyond the scope
of this document.

Speech
~~~~~~

Speech is supported. It's enabled by default. Pressing Space+VolumeUp enables
it, and pressing Space+VolumeDown disables it. Another (legacy) way to disable
it is to press VolumeDown and VolumeUp together.

* To interrupt what's currently being spoken, press Backward+VolumeDown.

* To speak the current line, press Backward+VolumeUp. Another (legacy) way to
  do so is to press Space+c (dots 14).

Adjusting the Volume:
  * Press Dot1+VolumeDown for **softer**.
  * Press Dot1+VolumeUp for **louder**.

  The speech volume is set relative to the system volume. If, therefore, the
  speech volume is set as high as it'll go but still sounds too soft then try
  increasing the system volume (by pressing VolumeUp by itself).

Adjusting the Rate:
  * Press Dot2+VolumeDown for **slower**.
  * Press Dot2+VolumeUp for **faster**.

Adjusting the Pitch:
  * Press Dot3+VolumeDown for **lower**.
  * Press Dot3+VolumeUp for **higher**.

Adjusting the Balance:
  * Press Dot4+VolumeDown for **more left**.
  * Press Dot4+VolumeUp for **more right**.

Sleep Talk Mode
```````````````

Sleep Talk Mode leaves speech active while the Power switch is off. This
capability is useful when, for example, you'd like to reduce battery drain but
still be informed when an important asynchronous event, e.g. the arrival of a
notification, occurs.

This mode is disabled by default. Pressing Forward+VolumeUp enables it, and
pressing Forward+VolumeDown disables it.

Differences from Braille Rendering
``````````````````````````````````

Speech is rendered differently than braille is in the following ways:

* The [brackets] around screen element descriptions provided by application
  developers aren't spoken.

* The {braces} around screen element types aren't spoken.

* A space is inserted in between each pair of a lowercase letter followed by an
  uppercase letter within screen element types in order to improve the way that
  each implied word is pronounced. For example, ``SeekBar`` is spoken as
  ``Seek Bar``.

* The (parentheses) around screen element states aren't spoken.

* The state of a checkbox (or switch) is spoken as either ``checked`` or
  ``not checked``.

Braille
~~~~~~~

The On-screen Monitor
`````````````````````

An on-screen monitor that shows what's on the braille display can be enabled
from `The Settings Screen`_. It shows both the braille cells and the text.

Adjusting the Dot Firmness
``````````````````````````

The firmness of the braille dots can be adjusted from `The Settings Screen`_.

Disabling the Display
`````````````````````

The braille display is enabled by default. It can be disabled by pressing
Backward+Dot1, and reenabled by pressing Forward+Dot4.

Developer Mode
~~~~~~~~~~~~~~

A very intentionally difficult-to-press key combination has been defined for
enabling and disabling Developer Mode::

  Backward + Forward + Dot1 + Dot2 + Dot4 + Dot5

A long press enables it, and a short press disables it. Note that
this mode can't be enabled unless `Long Press Mode`_ is enabled (another
accident prevention scheme).

When this mode is enabled:

* Additional key combinations are enabled (see `Developer Operations`_).

* The battery indicators line (see `Checking Status Indicators`_) includes the
  battery's voltage and temperature.

* If a component of the |user interface| crashes, an email containing the Java
  backtrace of that crash is sent to the |product name| developers for
  analysis. The email doesn't contain any sensitive information.


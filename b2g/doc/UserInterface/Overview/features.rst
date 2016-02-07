Useful Features
---------------

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

Managing the Home Screen
````````````````````````

To add a shortcut to the home screen:

1) Go to the home screen.
2) Click on ``Apps``.
3) Navigate to the label of the application that you'd like to add.
4) Press Space+Dot8+s (dots 234).

To remove a shortcut from the home screen:

1) Go to the home screen.
2) Navigate to the label of the application that you'd like to remove.
3) Press Space+Dot7+s (dots 234).

If you'd rather not search for the application's label then you can
find an empty line within any input area (see `Input Areas`_) and type it.
You only need to type enough characters to uniquely identify it
among all of the applications that are installed on your |product name|.
Letters can be in either lower or upper case,
and the characters don't need to be at the beginning or end of the label.

When you're finished typing, press the key combination for the shortcut action
that you'd like to perform (see above). If the characters match the labels of
more than one application then the list will be presented within a popup
(see `Popups`_).
If this happens then navigate to the one you'd like to add and press Center.

Builtin Shortcuts
`````````````````

Shortcuts to Useful Android Screens
'''''''''''''''''''''''''''''''''''

The following shortcuts to useful Android screens are provided:

Space+p (dots 1234)
  Go to the Power Off screen.

Space+? (dots 1456)
  Go to the currently registered **assist** app. The default is
  ``Google Now``.

Space+s (dots 234)
  Begin a shortcut. If one of the following key combinations is entered within
  |intermediate action timeout|
  then the action it's bound to is immediately invoked.
  If not, then an `Action Chooser`_ is presented
  that let's you pick from the following choices.

  a (dot 1)
    Go to the Accessibility Settings screen.

  b (dots 12)
    Go to the Bluetooth Settings screen.

  c (dots 14)
    Go to the Calendar app.

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
    Initiate a voice command.

  w (dots 2456)
    Go to the Wi-Fi Settings screen.

  # (dots 3456)
    Go to the Calculator app.

Shortcuts to Other |product name| Applications
''''''''''''''''''''''''''''''''''''''''''''''

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


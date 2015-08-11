Introduction
============

Physical Description
--------------------

The Top
~~~~~~~

The eight, rectangular, concave keys (in two groups of four) near the 
back of the top comprise a standard, eight-dot, braille keyboard. The 
four on the left, from left to right, are dots 7, 3, 2, and 1. The four 
on the right, from left to right, are dots 4, 5, 6, and 8.

* The Dot7 key is often referred to as the Backspace key.
* The Dot8 key is often referred to as the Enter key.

The square pad with a small, round button in the middle, in between the Dot1
and Dot4 keys, is the five-key D-Pad (directional pad). Its four edges are the
Up, Down, Left, and Right keys. The button in the middle is the Center
key.

The long, rectangular key in front of the Dot1 key, the D-Pad, and the Dot4 key
is the Space key (or bar).

The braille display (20 eight-dot cells) is at the front of the top. 
There's a small, round button just behind each cell - these are the 
cursor routing keys. The convex, square button to the left of the cursor 
routing keys is the Backward key, and the one to their right is the Forward
key.

The Front
~~~~~~~~~

Two internal speakers (for the left and right stereo channels) are,
correspondingly, at the left and right ends of the front.

The internal microphone is just behind a very small slit in the middle of 
the front.

The Left Side
~~~~~~~~~~~~~

The earphone/microphone jack (3.5mm) is near the front of the left side.
Standard cell phone earphones can be used.

The video jack (RCA) is in the middle of the left side. It's for an
external video monitor. This provides an easy way for a sighted person,
e.g. a teacher, to offer direct assistance.

A standard USB-A (or host) port is near the back of the left side. USB 
devices (memory stick, external keyboard, mouse, Ethernet adapter, hub, etc)
can be connected to it. Devices that draw power from the port will drain the
battery so using a powered hub, when possible, should be considered.

The Right Side
~~~~~~~~~~~~~~

The volume controls are the two, small, round buttons near the front of 
the right side. The one toward the front is the Volume Down key, and the 
one toward the back is the Volume Up key. These keys, when pressed on their
own, adjust the volume of the current Android audio stream.

The Power Switch
````````````````

The Power switch is the slide switch near the back of the right side. 
It's **off** position is toward the front, and its **on** position is 
toward the back.

If the system is shut down, then:

* Switching it on boots the device. You should hear one short beep. If,
  instead, you hear three short beeps then the battery is too low and the
  device won't boot. In this case, switch it back off, and then charge the
  battery for a while before trying again.

* Switching it on while holding VolumeDown boots the device into Recovery Mode.

If the system is running, then:

* Switching it off puts the system to sleep.

* Switching it on wakes the system up.

When the system is either booted or woken up, the following settings are reset
to their default values:

* Input Mode is set to Text.
* One Hand Mode is disabled.
* Braille is enabled.

The Back
~~~~~~~~

The SD card slot is the long slot in the middle of the back.

A micro USB-B (or device) port is near the left end of the back. It's 
primary use is for charging the battery. The battery can be charged from 
a computer's USB port.

The Bottom
~~~~~~~~~~

The camera's lens is the small circle on the bottom that's about 2cm 
(3/4 of an inch) in from the middle of the right side. The rectangular 
window that's just a little further in is where the flash LEDs are.

The Reset Button
````````````````

The Reset button is hidden within a very small, round hole on the 
bottom. If the device is turned over, front to back, the hole is at 
about the eight o'clock position relative to, and very close to, the foot
nearest to the Power switch. A thin, round object, like a pin or paperclip, is
needed.

Try to keep the object you're using as perpendicular to the bottom as you can.
You only need to press gently. If it feels like you're pressing against a hard
surface then you've missed the button. You should feel a soft click. You should
also hear a long, high-pitched beep.

The Removable Cover
```````````````````

There's a removable, rectangular, plastic cover in the middle of the 
bottom. To remove it:

1) Turn the device over so that its bottom is on top.

2) Press gently down on the cover, and then slide it toward the side where
   the Power switch is.

3) Lift the end of the cover that's toward the side where the earphone
   jack is.

Be careful after you've removed the cover because the underside of a circuit 
board will be exposed. Removing the cover is how to gain access to the 
following:

.. comment

  * The Micro-SD card slot, which is in the corner that's toward the back and
    toward the side where the earphone jack is. The open end of the slot is
    toward the side where the Power switch is. To insert a card, gently press it
    in until you hear a click. To remove the card, gently press it until you hear
    a click, and then allow the slot's internal spring to push it out.

* The Micro-SIM card slot, which is in the corner that's toward the front and
  toward the side where the Power switch is. To gain access to the actual slot,
  slide its cover slightly toward the front and then lift the end of it that's
  toward the back. As with most SIM card slot covers, its hinge is fragile so
  be gentle.

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

Typing
------

Directly Typing Regular Text
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Backward+Dot1 restores the input mode to **text** (the default). Either booting
the device or waking it up (see `The Power Switch`_) automatically resotres
input to this mode.

In this mode, any character that has a defined braille representation (see
`Braille Character Map`_) can be directly typed by pressing the corresponding
keys as a single combination. There are two exceptions to this simple (and
obvious) rule. If the defined representation of a character is either just
dot 7 or just dot 8 then it can't be directly typed because those keys are,
respectively, Backspace and Enter.

Typing a Control Character
``````````````````````````

In order to type a control character, press Space+x (dots 1346) immediately
before the letter or special symbol that represents it (see `ASCII Control
Characters`_). For example, in order to type a tab (which happens to be control
I), press Space+x and then immediately type the letter ``i``.

The letter or special symbol must be typed within
|intermediate action timeout|.

Directly Typing Unicode Braille Characters
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Forward+Dot4 sets the input mode to **braille**.

In this mode, the characters that render in text as actual braille cells (see
`Unicode Braille Characters`_) can be directly typed by pressing the
corresponding keys as a single combination. Two of these characters can't be
directly typed - just dot 7 (because it's the Backspace key), and just dot 8
(because it's the Enter key). Both of these can still be indirectly typed (see
`Indirectly Typing Any Character`_).

* The Unicode value for a braille cell containing just dot 7 is U+2840.
* The Unicode value for a braille cell containing just dot 8 is U+2880.

These characters have several uses, including (but not limited to):

* Actual braille characters can be written into text documents.

* Contracted braille can be accurately saved.

* Braille music can be accurately saved, and also shared with others who use
  different localized braille character mappings.

Indirectly Typing Any Character
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Any character can always be indirectly typed, even if directly typing it isn't 
possible:

* Its braille representation hasn't been defined.

* You don't know its braille representation.

* It's represented in braille by either just dot 7 (the Backspace key) or just
  dot 8 (the Enter key).

In order to indirectly type it, press Space+u (dots 136). This brings up a
prompt (see `Prompts`_) with the following header::

  Unicode> U+

``U+`` is the conventional prefix for the hexadecimal value of a Unicode 
character. The prompt allows you to enter up to four hexadecimal digits -
``0`` through ``9`` and ``A`` through ``F`` (either upper or lower case).
Leading zeroes may be omitted. The Unicode values of characters are, of course,
beyond the scope of this document.

The digits you enter may be freely edited, e.g. the cursor can be moved, any
digit may be deleted, a new digit may be inserted, etc. In other words, making
corrections doesn't require backspacing and retyping. When you're done, press
Enter.

If no digits have been entered yet (or if they've all been deleted) then you'll
only see the header. If, however, at least one digit has been entered then the
character represented (so far) by the digit(s) is displayed just to the right,
and that character's formal name is displayed on the next line.

To illustrate, let's see how a lowercase ``s`` would be indirectly typed. It's
Unicode value is U+0073. In order to keep this example simple, let's skip the
two leading zeroes.

.. topic:: Indirectly Typing the Letter ``s`` (U+0073)

  1) Press Space+u (dots 136)::

       Unicode> U+

  2) Type the digit ``7`` (dots 2356)::

       Unicode> U+7 ⣛
       bell

  3) Type the digit ``3`` (dots 25)::

       Unicode> U+73 ⠎
       latin small letter s

  4) Press Enter. The prompt goes away, and the ``s`` is typed.

Braille Rendering
-----------------

Characters
~~~~~~~~~~

The basic characters are rendered using the `North American Braille Computer
Code`_. Internally-designed representations for `Additional Common Symbols`_,
as well as for `Additional Characters for Spanish`_, have also been defined.

If the braille representation for a complex character (e.g. a letter with an
accent) hasn't been defined, then its base character (e.g. just the latter) is
displayed (unless, of course, even the base character's representation hasn't
been defined).  

The characters that render in text as actual braille cells (see `Unicode
Braille Characters`_) needn't (and, in fact, shouldn't) be defined. They're
always rendered in braille as themselves.

The special character rendered as |the undefined character| is used to
represent a character that doesn't have a defined representation.

Identifying an Unrecognized Character
`````````````````````````````````````

If you encounter a character that you don't recognize, then you can find out
what it is by holding Dot7 while pressing the cursor routing key behind that
character. This will cause a description of the character to be presented
within a popup (see `Popups`_). For example, the description for the letter
``s`` is::

  latin small letter s
  Code Point: U+0073
  Block: basic latin
  Category: lowercase letter
  Directionality: left to right

Special Symbols
~~~~~~~~~~~~~~~

When within an input area (see `Input Areas`_):

* The character where the cursor is is highlighted by |the cursor indicator|.
  This is its default representation. It can be changed via `The Settings
  Screen`_.

* Each of the currently selected characters is highlighted by |the selection
  indicator|. This is its default representation. It can be changed via `The
  Settings Screen`_.

Note that the cursor isn't shown when at least one character has been selected
(see `Selecting Text`_). This is because editing actions are then performed on
all of the selected characters as a single unit rather than at the cursor's
location.

If a screen element (list, group of pages, etc) needs to be scrolled, then:

* Scrolling forward (down or to the right) is indicated via
  |the scrolling forward symbol|.

* Scrolling backward (up or to the left) is indicated via
  |the scrolling backward symbol|.

Descriptive Annotations
~~~~~~~~~~~~~~~~~~~~~~~

A **checkbox** is rendered as either a space (meaning unchecked) or an 
``X`` (meaning checked) enclosed within [brackets], followed by its
label. For example::

  [ ] This box is not checked.
  [X] This box is checked.

A **switch** is rendered as though it were a **checkbox**. The box is checked
if the switch is in its **on** position, and unchecked if it's in its **off**
position. For example::

  [ ] Off
  [X] On

If a screen element has no text of its own but its developer has 
provided a textual description of it, then that description, enclosed 
within [brackets], is displayed. For example::

  [More options]
  [Navigate up]

If meaningful text for a screen element can't be found then it is
rendered as its widget type enclosed within {braces}. For example::

  {ImageButton}
  {SeekBar}

If a control isn't enabled then the word ``disabled``, enclosed within
(parentheses), is appended to its descriptive text. For example::

  OK (disabled)

When an action is performed that's neither implicitly confirmed (by an 
expected change on the braille display) nor explicitly confirmed (by a 
sound) then it's confirmed by a message (see `Messages`_). For example::

  One Hand On
  Long Press Off

Braille-only Dialogs
--------------------

A number of braille-only dialogs are used in order to directly communicate to
or interact with the user. They don't appear on an external video monitor.

Messages
~~~~~~~~

Messages are used to give feedback to the user for significant events. These
include (but aren't limited to):

* The successful changing of a setting.

* The timeout of a partially entered request, e.g. the control modifier has
  been pressed but the (then required) letter or special symbol hasn't been
  typed quickly enough (see `Typing a Control Character`_).

A message is a single-line, read-only dialog. No navigation may be performed
within it. It remains on the braille display for |message time|.

Popups
~~~~~~

Popups are used to present user-requested data as well as important system
information. This includes (but isn't limited to):

* The arrival of an Android notification.

* The description of a character (see `Identifying an Unrecognized
  Character`_).

* The values of various status indicators (see `Checking Status Indicators`_).

* Information that identifies the build (see `Identifying the Build`_).

A popup is a multi-line, read-only dialog. Normal navigation may be
performed within it. Dismiss it by pressing Enter. It's automatically dismissed
if no navigation operations have been performed within it for |popup time|.

Prompts
~~~~~~~

Prompts are used to request information from the user. This includes (but isn't
limited to):

* Requesting the value of a Unicode character (see `Indirectly Typing Any
  Character`_).

* Requesting the text to search for within the current screen element (see
  `Finding Text within the Current Screen Element`_).

A prompt is a read-write dialog. Normal navigation and editing may be performed
within its response area. Press Enter once the requested information has been
entered.

The prompt's header and response area are always on the first line. The
|user interface| may add helpful information to the rest of the first line
and/or to additional lines.  

The response to a prompt is remembered. The response area of a prompt is
initially empty, but, from then on, it's initialized to the previous response
entered for that prompt. The remembered response is selected (see `Selecting
Text`_) so that it can be easily replaced.

Additional Features
-------------------

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
  Go to a Settings screen. Another key combination must be pressed within
  |intermediate action timeout| in order to specify which Settings screen to go
  to:

  Space
    Go to the main Settings screen.

  a (dot 1)
    Go to the Accessibility Settings screen.

  b (dots 12)
    Go to the Bluetooth Settings screen.

  w (dots 2456)
    Go to the Wi-Fi Settings screen.

To Other |product name| Applications
````````````````````````````````````

The following shortcuts to other |product name| applications are provided:

Space+ed (dots 1246)
  Go to the |product name| editor.

Describing how any of these applications work is, of course, beyond the scope
of this document.

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

  Battery: 30%
  Wi-Fi: MyNetwork 60% 54Mbps

Identifying the Build
~~~~~~~~~~~~~~~~~~~~~

There may be times when you need to identify the exact build of the
|user interface| that you're currently using. For example, this information is
particularly helpful when you're reporting a problem or checking if there's a
newer version.

You can get this information by pressing Space, Backward, and Forward
together. This will cause details that identify the build to be presented
within a popup (see `Popups`_) that looks something like this::

  Build Details
  Time: 2015-07-22 00:34:02 UTC
  Revision: d3d8210

Speech
~~~~~~

Speech is supported. It's enabled by default. Pressing Space+VolumeUp enables
it, and pressing Space+VolumeDown disables it. Another (legacy) way to disable
it is to press VolumeDown and VolumeUp together.

* To interrupt what's currently being spoken, press Backward+VolumeDown.

* To speak the current line, press Backward+VolumeUp. Another (legacy) way to
  do so is to press Space+c (dots 14).

* To adjust the volume: press Dot1+VolumeDown for **softer**, and Dot1+VolumeUp
  for **louder**.

* To adjust the rate: press Dot2+VolumeDown for **slower**, and Dot2+VolumeUp
  for **faster**.

* To adjust the pitch: press Dot3+VolumeDown for **lower**, and Dot3+VolumeUp
  for **higher**.

* To adjust the balance: press Dot4+VolumeDown for **more left**, and
  Dot4+VolumeUp for **more right**.

Sleep Talk Mode
```````````````

Sleep Talk Mode leaves speech active while the Power switch is off. This
capability is useful when, for example, you'd like to reduce battery drain but
still be informed when an important asynchronous event, e.g. the arrival of a
notification, occurs.

This mode is disabled by default. Pressing Forward+VolumeUp enables it, and
pressing Forward+VolumeDown disables it.

The Settings Screen
~~~~~~~~~~~~~~~~~~~

Pressing Space+o (dots 135) takes you to the settings screen for the
|user interface|.

The top line contains the following buttons:

Save Settings
  Save the current values of the settings for later restoration. This is a good
  way to checkpoint the configuration that you're most comfortable with.

Restore Settings
  Restore the setting values that were most recently saved. This is how to get
  back to the configuration that you're most comfortable with.

Reset to Defaults
  Restore the setting values to an internally-defined configuration. This is
  how to recover if, for example, you've accidentally messed up your saved 
  settings.

Each subsequent line contains a setting that can be changed, and is laid out in
columns as follows:

1) This column contains the name of the setting.

2) If it's a boolean setting then this column contains an on/off switch. If
   it's any other kind of setting then this column shows that setting's current
   value.

3) For non-boolean settings, this column contains a button that changes the
   setting to its *previous* (lower for numeric settings) value.

4) For non-boolean settings, this column contains a button that changes the
   setting to its *next* (higher for numeric settings) value.

General Settings
````````````````

The following settings (as of the time of this writing) are presented:

.. include:: tbl-settings-general.rst

Developer Settings
``````````````````

If `Developer Mode`_ is enabled, then these additional settings are presented:

.. include:: tbl-settings-developer.rst

Developer Mode
~~~~~~~~~~~~~~

A very intentionally difficult-to-press key combination has been defined for
enabling and disabling Developer Mode::

  Backward + Forward + Dot1 + Dot2 + Dot4 + Dot5

A long press enables it, and a short press disables it. Note that this means
that this mode can't be enabled unless `Long Press Mode`_ is enabled (another
accident prevention scheme).

See `Developer Operations`_ for a list of additional key combinations that this
mode defines.


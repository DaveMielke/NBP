Usage Notes
===========

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

The square with a small, round button in the middle, in between dots 1 
and 4, is the five-key D-Pad (directional pad). Its four edges are the
Up, Down, Left, and Right keys. The button in the middle is the Center
key.

The long, rectangular key in front of dot 1, the D-Pad, and dot 4 is the 
Space key (or bar).

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
devices (memory stick, external keyboard, mouse, ethernet adapter, hub, etc)
can be connected to it. Devices that draw power from the port will drain the
battery so using a powered hub, when possible, should be considered.

The Right Side
~~~~~~~~~~~~~~

The volume controls are the two, small, round buttons near the front of 
the right side. The one toward the front is the Volume Down key, and the 
one toward the back is the Volume Up key.

The Power switch is the slide switch near the back of the right side. 
It's **off** position is toward the front, and its **on** position is 
toward the back.

The Back
~~~~~~~~

The SD card port is the long slot in the middle of the back.

A micro USB-B (or device) port is near the left of the back. It's 
primary use is for charging the battery. The battery can be charged from 
a computer's USB port.

The Bottom
~~~~~~~~~~

The camera's lens is the small circle on the bottom that's about 2cm 
(3/4 of an inch) in from the middle of the right side. The rectangular 
window that's just a little further in is where the flash LEDs are.

The Reset Switch
````````````````

The Reset button is hidden within a very small, round hole on the 
bottom. If the device is turned over, front to back, the hole is at 
about the eight o'clock position, very close to the foot nearest to the 
Power switch. A thin, round object, like a pin or paperclip, is needed.

Try to keep the object you're using as perpendicular to the bottom as you can.
You only need to press gently. If it feels like you're pressing against a hard
surface then you've missed the button. You should feel a soft click. You should
also hear a long, high-pitched beep.

If you also hear three short beeps then the battery is too low, and you'll need
to charge the battery for a while before you'll be able to successfully boot
the device.

The Removable Cover
```````````````````

There's a removable, rectangular, plastic cover in the middle of the 
bottom. To remove it:

1) Turn the device over so that its bottom is on top.

2) Press gently down on the cover and slide it toward the side where
   the power switch is.

3) Lift the end of the cover that's toward the side where the earphone
   jack is.

Be careful after you've removed the cover because the underside of a circuit 
board will be exposed. Removing the cover is how to gain access to the 
following:

* The Micro-SD card slot.
  It's in the corner that's toward the back and toward the side where the 
  earphone jack is. The open end of the slot is toward the side where the 
  power switch is. To insert a card, gently press it in until you hear a 
  click. To remove the card, gently press it until you hear a click, and 
  then allow the slot's internal spring to push it out.

* The SIM card slot.
  It's in the corner that's toward the front and toward the side where the 
  power switch is. To gain access to the actual slot, lift the end of its 
  cover that's toward the back. As with most SIM card slot covers, its 
  hinge is fragile so be gentle.

Key Combinations
----------------

A key combination may include any number of keys (including just one). 
There is only one restriction on which keys may be used to form a key 
combination, namely that only one cursor routing key may be used at a 
time. If more than one cursor routing key is pressed at the same time, 
the first one that was pressed is used.

If any of the keys of a combination is released before the long press timeout
(see `Long Press Mode`_) expires then the action bound to that combination is
executed. If the timeout expires then the action is immediately executed
even though none of the keys has been released.

The way to cancel a key (or keys) that has been accidentally pressed is to
press enough additional keys such that an unbound key combination is being
attempted. A key combination that's been formally defined for this purpose is
all eight dots plus Space.

Long Press Mode
~~~~~~~~~~~~~~~

A secondary action may be bound to a key combination. A key combination's
secondary action is executed if all of its keys are held until the long press
timeout expires when Long Press Mode is enabled.

This mode is enabled by default. Pressing Forward+Dot6 enables it,
and pressing Backward+Dot3 disables it.

The long press timeout is |long press timeout|.

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

One Hand Mode is off by default. Pressing Forward+Dot8 enables it, and pressing
Backward+Dot7 (followed, of course, by pressing Space) disables it. Both of
these key combinations have been chosen so that they can be reasonably easily
pressed with a single hand.

Switching the power switch **off** and then back **on** automatically disables
One Hand Mode. This provides an intuitive way for a user who has accidentally
enabled this mode, and who may not know how to disable it, to easily revert the
keyboard to normal operation.

Another (legacy) way to enable One Hand Mode is to hold dot 8 while switching
the power on.

Braille Rendering
-----------------

The basic characters are rendered using the `North American Braille Computer
Code`_. Internally-designed representations for `Additional Common Symbols`_,
as well as for `Additional Characters for Spanish`_, have also been included.

The special character rendered as |the undefined character| is used to
represent a character that doesn't have its own defined representation.

When on an editable text field:

* The character immediately to the right of the cursor is highlighted by
  |the cursor indicator|.

* Each of the currently selected characters is highlighted by
  |the selection indicator|. 

Note that the cursor isn't shown when at least one character has been 
selected. This is because edit actions are then performed on all of the
selected characters as a single entity rather than at the cursor's location.

If a screen element (list, group of pages, etc) needs to be scrolled, then:

* Scrolling forward (down or to the right) is indicated via
  |the scrolling forward symbol|.

* Scrolling backward (up or to the left) is indicated via
  |the scrolling backward symbol|.

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
attached a textual description to it, then that description, enclosed 
within [brackets], is displayed. For example::

  [More options]
  [Navigate up]

If meaningful text for a significant screen element can't be found then it is
rendered as its widget type enclosed within {braces}. For example::

  {ImageButton}
  {SeekBar}
  {ViewPager}

If a control isn't enabled then the word ``disabled``, enclosed within
(parentheses), is appended to its descriptive text. For example::

  OK (disabled)

When an action is performed that is neither implicitly confirmed (by an 
expected change on the braille display) nor explicitly confirmed (by a 
sound) then it's confirmed by a short message that's displayed for a brief
period (|message hold time|). For example::

  One Hand On
  Long Press Off

Text Selection
--------------

When on an editable text field, any sequence of text within that field 
can be easily **selected**. This is how to do it:

1) If necessary, use the Forward and Backward keys to pan to a place where the
   first character to be selected can be seen.

2) Hold the Backward key while pressing the cursor routing key behind the 
   character that's to start the selection.

3) If necessary, use the Forward and Backward keys to pan to a place where the
   last character to be selected can be seen.

4) Hold the Forward key while pressing the cursor routing key behind the 
   character that's to end the selection.

Steps 1 and 2 may be reperformed at any time in order to change the start of
the selection. Likewise, steps 3 and 4 may be reperformed at any time in order
to change the end of the selection. In fact, the end of the selection can be
set before the start of the selection has been set, i.e. steps 3 and 4 may be
performed before steps 1 and 2 have been performed.

The current text selection is highlighted via |the selection indicator|.
Selecting text is useful in at least the following ways:

* Typing a character deletes the selected text, puts the cursor where
  the selected text was, and then inserts the typed character at that point.
  This, in other words, is an efficient way to replace old text with new text.
  Just select the old text, and then start typing the new text.

* Pressing any of the delete keys (see `Editable Text Fields`_) removes all of
  the selected text. This, in other words, is an efficient way to delete a
  block of text. Just select it, and then delete it.

If the start of the selection is set first, and if the cursor is after that
character, then the selection is implicitly extended forward to (but not
including) the character where the cursor is. This, for example, provides an
easy way to delete or replace several characters that have just been typed.

If the end of the selection is set first, and if the cursor is before that
character, then the selection is implicitly extended back to (and including)
the character where the cursor is. This, for example, provides an easy way to
replace a word, line, paragraph, etc.


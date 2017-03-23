Usage
=====

Screen Layout
-------------

.. topic:: Layout Legend

  .. include:: layout-legend.rst

Portrait
~~~~~~~~

The screen has the following layout when in portrait mode:

.. include:: layout-portrait.rst

Landscape
~~~~~~~~~

The screen has the following layout when in landscape mode:

.. include:: layout-landscape.rst

Result Line
-----------

Expression Line
---------------

Enter Key
~~~~~~~~~

Shortcuts
~~~~~~~~~

.. include:: expression-shortcuts.rst

Expression History
~~~~~~~~~~~~~~~~~~

Control Buttons
---------------

Clear
~~~~~

Pressing this button clears the `expression line`_.

Alternate Keypad
~~~~~~~~~~~~~~~~

Repeatedly pressing this button cycles through all of the keypads
associated with the current `calculator mode`_.
A long press switches to the mode's primary keypad.
This button is disabled if the mode only has one keypad.

Calculator Mode
~~~~~~~~~~~~~~~

Repeatedly pressing this button cycles through all of the calculator modes.
Its label is a three-character abbreviation for the currently selected mode.
A long press presents a dialog from which the desired mode can be selected.

The following modes are available:

.. _Decimal Calculator Mode:

DEC
  Decimal (the default).
  The keypads associated wit this mode are:

  * `Decimal Keypad`_
  * `Function Keypad`_
  * `Conversion Keypad`_

.. _Hexadecimal Calculator Mode:

HEX
  Hexadecimal.
  The keypads associated wit this mode are:

  * `Hexadecimal Keypad`_

Decimal Notation
~~~~~~~~~~~~~~~~

Repeatedly pressing this button cycles through all of the decimal number notations.
Its label is a three-character abbreviation for the currently selected notation.
A long press presents a dialog from which the desired notation can be selected.

The following notations are available:

FXD
  Fixed (the default).

SCI
  Scientific.

ENG
  Engineering.

Angle Unit
~~~~~~~~~~

Repeatedly pressing this button cycles through all of the trigonometric angle units.
Its label is a three-character abbreviation for the currently selected unit.
A long press presents a dialog from which the desired unit can be selected.

The following units are available:

DEG
  Degrees (the default).
  A circle is divided into 360 degrees.

RAD
  Radians.
  A circle is divided into 2π radians.

GON
  Gradians (also known as grads or gons).
  A circle is divided into 400 gradians.

Navigation Buttons
------------------

.. include:: buttons-navigation.rst

Left
~~~~

Pressing this button moves the cursor one character to the left.
A long press moves the cursor all the way to the left.
This button is disabled when the cursor is to the left of the first character.

Right
~~~~~

Pressing this button moves the cursor one character to the right.
A long press moves the cursor all the way to the right.
This button is disabled when the cursor is to the right of the last character.

Backspace
~~~~~~~~~

Pressing this button deletes the character immediately to the left of the cursor.
A long press deletes all of the characters to the left of the cursor.
This button is disabled when the cursor is to the left of the first character.

Delete
~~~~~~

Pressing this button deletes the character immediately to the right of the cursor.
A long press deletes all of the characters to the right of the cursor.
This button is disabled when the cursor is to the right of the last character.

Up
~~

Pressing this button moves to the previous entry in the `expression history`_.
A long press moves to the earliest (oldest) entry.
This button is disabled when on the earliest entry.

Down
~~~~

Pressing this button moves to the next entry in the `expression history`_.
A long press moves to the latest (most recent) entry.
This button is disabled when on the latest entry.

Memory Buttons
--------------

.. include:: buttons-memory.rst

Variable
~~~~~~~~

Pressing this button presents a list of
the user-defined variables followed by the `predefined variables`_.
Each line contains the name and current value of a variable.
For a predefined variable, it also contains its description.

Selecting a variable from this list
enters its name into the `expression line`_.
The following actions are also available:

Cancel
  Dismiss the list without taking any action.

Store
~~~~~

Pressing this button presents a list of the user-defined variables.
Each line contains the name and current value of a variable.

Selecting a variable from this list
assigns the value currently shown on the `result line`_ to it.
The following actions are also available:

Cancel
  Dismiss the dialog without taking any action.

New
  Present a dialog for entering the name of a new variable.
  This dialog doesn't allow an existing variable to be overwritten.
  It offers the following actions:

  Cancel
    Dismiss the dialog without taking any action.

  Store
    Assign the value currently shown on the `result line`_ to to the new variable.
    This action is disabled when the name field is empty
    and when it contains the name of an existing variable.

Erase
~~~~~

Pressing this button presents a list of the user-defined variables.
Each line contains the name and current value of a variable.

Selecting a variable from this list removes its definition.
The following actions are also available:

Cancel
  Dismiss the dialog without taking any action.

A long press presents the same list in a way that allows
several variable definitions to be removed at once.
It offers the following additional actions:

Erase
  Remove the definitions of all the variables that have been selected.

Function
~~~~~~~~

Pressing this button presents a list of the `predefined functions`_.
Each line contains the name and description of a function.

Selecting a function from this list
enters its name, followed by an open bracket, into the `expression line`_.
The following actions are also available:

Cancel
  Dismiss the dialog without taking any action.

Keypad
------

Decimal Keypad
~~~~~~~~~~~~~~

This is the primary keypad for the `Decimal calculator mode`_.
It has the following layout:

.. include:: keypad-decimal.rst

Function Keypad
~~~~~~~~~~~~~~~

This is the second keypad for the `Decimal calculator mode`_.
It has the following layout:

.. include:: keypad-function.rst

Conversion Keypad
~~~~~~~~~~~~~~~~~

This is the third keypad for the `Decimal calculator mode`_.
It has the following layout:

.. include:: keypad-conversion.rst

Hexadecimal Keypad
~~~~~~~~~~~~~~~~~~

This is the primary keypad for the `Hexadecimal calculator mode`_.
It has the following layout:

.. include:: keypad-hexadecimal.rst


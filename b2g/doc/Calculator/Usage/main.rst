Usage
=====

This calculator has been designed to be as usable as possible
not only by sighted people
but also by blind people using speech and/or a braille device.
It's easy to use via
it's own on-screen calculator-style keys,
a full on-screen keyboard,
or an externally connected Bluetooth or USB keyboard.

It offers the following features:

* Real and complex decimal operations.
* Fixed, scientific, and engineering notation.
* Trigonometric functions that support radians, degrees, and gradians.
* Unit conversion.
* Integer and bitwise hexadecimal operations.
* Predefined variables for common scientific constants.
* Management of persistent user-defined variables.

Screen Layout
-------------

This subsection contains screen layout images
that've been intentionally drawn using traditional ASCII art.
This has been done so that a blind person can easily "look" at them
because screen readers are notoriously poor at dealing with graphical images.

.. topic:: Layout Legend

  The `Store`_, `Erase`_, and `f(x)`_ button labels are literal.
  The rest of the button labels require some explanation:

  .. include:: layout-legend.rst

Portrait
~~~~~~~~

The screen has the following layout when in portrait mode:

.. include:: layout-portrait.rst
  :literal:

Landscape
~~~~~~~~~

The screen has the following layout when in landscape mode:

.. include:: layout-landscape.rst
  :literal:

Result Line
-----------

This output area displays the on-going current result of evaluating
what's been entered in the `expression line`_.
If no data has been entered yet then ``0`` is displayed.
If the expression can't be evaluated at the moment
(e.g. due to missing data or syntax errors)
then the last known result, preceded by a question mark [?], is displayed.
The displayed value begins with zero or more open brackets [(] -
one for each bracket nesting level that hasn't yet been closed.
Expression evaluation only considers the current bracket nesting level.

To illustrate, here's what's displayed as the expression
``(2+3)×(4−(5+6)×7)``
is entered:

.. csv-table::
  :header: "Expression", "Result"

  "", "0"
  "(", "(?"
  "(2", "(2"
  "(2+", "(?2"
  "(2+3", "(5"
  "(2+3)", "5"
  "(2+3)×", "?5"
  "(2+3)×(", "(?5"
  "(2+3)×(4", "(4"
  "(2+3)×(4−", "(?4"
  "(2+3)×(4−(", "((?4"
  "(2+3)×(4−(5", "((5"
  "(2+3)×(4−(5+", "((?5"
  "(2+3)×(4−(5+6", "((11"
  "(2+3)×(4−(5+6)", "(−7"
  "(2+3)×(4−(5+6)×", "(?−7"
  "(2+3)×(4−(5+6)×7", "(−73"
  "(2+3)×(4−(5+6)×7)", "−365"

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
  This mode performs real and complex operations
  on IEEE 64-bit floating-point (commonly known as double) values.
  They consist of a 1-bit sign, an 11-bit exponent, and a 48-bit mantissa.

  The keypads associated wit this mode are:

  * `Decimal`_
  * `Function`_
  * `Conversion`_

.. _Hexadecimal Calculator Mode:

HEX
  Hexadecimal.
  This mode performs unsigned integer and bitwise operations on 64-bit values.
  Both input and output are in base 16 and use hexadecimal digits.
  Digits A through F are always displayed in uppercase
  but may be input in either case.
  A number may consist of up to 16 hexadecimal digits.

  The keypads associated wit this mode are:

  * `Hexadecimal`_

Decimal Notation
~~~~~~~~~~~~~~~~

Repeatedly pressing this button cycles through all of the decimal notations.
Its label is a three-character abbreviation for the currently selected notation.
A long press presents a dialog from which the desired notation can be selected.

The following notations are available:

FXD
  Fixed (the default).
  If the absolute value of the number
  is less than 10\ :sup:`13` [``1,000,000,000,000``]
  and greater than or equal to 10\ :sup:`-3` [``0.001``]
  then it's displayed with its decimal point in its natural position.
  Its power of ten multiplier (e.g. ×10\ :sup:`x`) isn't displayed
  (because it's 0),
  and digit grouping separators (e.g. 1,000) are displayed
  (to ease readability).
  If the number is an integer then the decimal point isn't displayed.

  If the number is outside this range then it's considered to be
  too difficult to intuitively read in fixed notation,
  and scientific notation is used instead.

SCI
  Scientific.
  The number is always displayed such that its absolute value
  is greater than or equal to 1 and less than 10.
  In other words, there's always exactly one digit before the decimal point.
  If the rest of the digits (other than the first one) are 0
  then the decimal point isn't displayed.
  If it needs to be multiplied by a power of 10
  (either positive or negative)
  then ×10\ :sup:`x` is appended.

ENG
  Engineering.
  This is identical to scientific notation except that
  the power of ten is always a multiple of 3
  and up to 3 digits are displayed before the decimal point.

In all cases, the sign is only displayed if the number is negative.
This is also true for the power of ten.

.. list-table:: Comparison of Decimal Notations

  * + Fixed
    + 0.0123
    + 456,789
  * + Scientific
    + 1.23×10\ :sup:`-2`
    + 4.56789×10\ :sup:`5`
  * + Engineering
    + 12.3×10\ :sup:`-3`
    + 456.789×10\ :sup:`3`

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

Memory Buttons
--------------

These keys provide easy access
to the `predefined variables`_
and to the `predefined functions`_.
They can also be used to manage user-defined variables.

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

f(x)
~~~~

Pressing this button presents a list of the `predefined functions`_.
Each line contains the name and description of a function.

Selecting a function from this list
enters its name, followed by an open bracket, into the `expression line`_.
The following actions are also available:

Cancel
  Dismiss the dialog without taking any action.

Navigation Buttons
------------------

These keys are particularly useful to blind people
who are using a touch screen and a speech-based screen reader.

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

Keypads
-------

Decimal
~~~~~~~

This is the primary keypad for the `Decimal calculator mode`_.
It has the following layout:

.. include:: keypad-decimal.rst

Function
~~~~~~~~

This is the second keypad for the `Decimal calculator mode`_.
It has the following layout:

.. include:: keypad-function.rst

Conversion
~~~~~~~~~~

This is the third keypad for the `Decimal calculator mode`_.
It has the following layout:

.. include:: keypad-conversion.rst

Hexadecimal
~~~~~~~~~~~

This is the primary keypad for the `Hexadecimal calculator mode`_.
It has the following layout:

.. include:: keypad-hexadecimal.rst


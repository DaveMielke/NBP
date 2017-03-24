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
A long press presents a list from which the desired mode can be selected.
The button's label is a three-letter abbreviation for the current selection.
The current selection is persistent.

The following modes are available:

.. _Decimal Calculator Mode:

DEC
  Decimal (the default).
  This mode performs real and complex operations
  on IEEE 64-bit floating-point (commonly known as double) values.
  They consist of a 1-bit sign, an 11-bit exponent, and a 52-bit significand.
  Other than 0 (which is a special case),
  the smallest absolute value that this representation supports is
  2\ :sup:`-1074` or approximately 5×10\ :sup:`−324`,
  and the largest absolute value that it supports is
  2\ :sup:`1023` or approximately 1.8×10\ :sup:`308`.
  Calculations are performed with an accuracy of 15 significant digits.

  This mode supports complex numbers.
  The imaginary component, if nonzero, is displayed as
  either an addition to or a subtraction from the real component.
  If a number doesn't have an imaginary component
  then only its real component is displayed.
  If it does have an imaginary component but doesn't have a real component
  then only the imaginary component is displayed.
  Here are some examples:

  * 0
  * 1
  * i
  * 2i
  * −3
  * −4i
  * 5 + i
  * −6 + 2i
  * −7 − 3i
  * 2.3×10\ :sup:`4` + 5.6×10\ :sup:`7`\ i

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
A long press presents a list from which the desired notation can be selected.
The button's label is a three-letter abbreviation for the current selection.
The current selection is persistent.

The following notations are available:

FXD
  Fixed (the default).
  If the absolute value of the number
  is less than 10\ :sup:`12` [``1,000,000,000,000``]
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
A long press presents a list from which the desired unit can be selected.
The button's label is a three-letter abbreviation for the current selection.
The current selection is persistent.

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


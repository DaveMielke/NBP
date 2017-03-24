Keypads
-------

Decimal
~~~~~~~

This is the primary keypad for the `Decimal calculator mode`_.
It has the following layout:

.. include:: keypad-decimal.rst

Most of these keys enter whatever their label says into the `expression line`_.
The exceptions are:

* The **=** key completes the expression.
  It's equivalent to typing the `Enter key`_.

* The x\ :sup:`y` key enters **^** (the actual exponentiation operator).

* The x\ |dot|\ 10\ :sup:`y` key enters **×10^**.
  It's for multiplying a number by a power of ten.

The **"** and **'** keys are for entering a number in
the ``degrees"minutes'seconds`` format.
Each of these components (**"minutes** and **'seconds**) is optional.
If specified, it's value must be an integer
that's greater than or equal to 0 and less then 60.
If omitted, it's value is assumed to be 0.
If any of these components is specified then a decimal point may not be used.

Function
~~~~~~~~

This is the second keypad for the `Decimal calculator mode`_.
It has the following layout:

.. include:: keypad-function.rst

Each of these keys enters its function's name,
followed by an open bracket [(],
into the `expression line`_.
The exceptions are **e** and **pi**,
which don't enter the open bracket because they're variables.

Conversion
~~~~~~~~~~

This is the third keypad for the `Decimal calculator mode`_.
It has the following layout:

.. include:: keypad-conversion.rst

Each four-key row is a separate key group.
This allows five different unit conversions to be configured.
Each of them, as shown above, defaults to a common US-to-International unit conversion.

The first three keys configure the conversion:

Type
  This key presents a list from which the desired unit type
  (e.g. `length`_, `area`_, `volume`_) can be selected.

From
  This key presents a list from which the unit that
  the supplied value is in can be selected.
  The list only contains units belonging to the currently configured type.

To
  This key presents a list from which the unit that
  the supplied value is to be converted to can be selected.
  The list only contains units belonging to the currently configured type.

The rightmost key is for actually performing
the currently configured unit conversion.
It's label shows what that conversion is in the form *from*\ 2\ *to*
(e.g. **sqmi2km**).
This is the name of the predefined function that performs the conversion.
Just like the keys on the `Function`_ keypad, pressing this key
enters the name of this function, followed by an open bracket [(],
into the `expression line`_.

Any of the unit conversion functions can be directly typed
into the `expression line`_.
Because there are so many, none of them is included
within the list presented by pressing the `f(x)`_ button.

The most recently configured conversion
for each unit type for each key group
is persistent.

Hexadecimal
~~~~~~~~~~~

This is the primary keypad for the `Hexadecimal calculator mode`_.
It has the following layout:

.. include:: keypad-hexadecimal.rst

Each of these keys enters whatever its label says into the `expression line`_.
The **0** through **9** and **A** through **F** keys
are the 16 hexadecimal digits.
The rest of the keys are as follows:

.. csv-table::
  :header: "Key", "Description"

  "\=", "expression completion"
  "\(", "open bracket"
  "\)", "close bracket"
  "\<", "logical left shift"
  "\>", "logical right shift"
  "\&", "bitwise and"
  "\|", "bitwise inclusive or"
  "\^", "bitwise exclusive or"
  "\~", "bitwise ones complement"
  "\+", "integer addition"
  "\−", "integer subtraction or bitwise twos complement"
  "\×", "integer multiplication"
  "\÷", "integer division"
  "\%", "integer remainder"


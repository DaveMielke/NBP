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

Variables can also be created or updated via the **=** operator within an expression.
For example:

* x = 1
* x = y = 2
* x = 3 + y = 4 + z = 5

To clarify: the last example sets z to 5, y to 9, and x to 12.

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


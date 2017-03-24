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


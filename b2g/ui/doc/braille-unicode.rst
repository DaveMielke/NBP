Actual braille cells are represented by the 256 characters within the Unicode
range U+2800 through U+28FF. Each of the eight bits within the low-order byte
of this range represents a braille dot within the cell, according to
ISO 11548-1, as follows:

.. table:: Unicode Braille Dots

  ===  ===========  =======  ========
  Dot  Hexadecimal  Decimal  Binary
  ---  -----------  -------  --------
  1    01           1        00000001
  2    02           2        00000010
  3    04           4        00000100
  4    08           8        00001000
  5    10           16       00010000
  6    20           32       00100000
  7    40           64       01000000
  8    80           128      10000000
  ===  ===========  =======  ========

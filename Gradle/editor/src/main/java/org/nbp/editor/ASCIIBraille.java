package org.nbp.editor;

import org.nbp.common.Braille;

public abstract class ASCIIBraille extends Braille {
  private final static byte ASCII_MINIMUM = 0X20;
  private final static byte ASCII_MAXIMUM = 0X7E;

  private final static byte ASCII_LETTER = 0X40;
  private final static byte ASCII_LOWERCASE = 0X20;

  public final static boolean isSymbol (byte symbol) {
    if (symbol < ASCII_MINIMUM) return false;
    if (symbol > ASCII_MAXIMUM) return false;
    return true;
  }

  private final static byte[] symbolToDots = new byte[] {
    /* 20   */ 0,
    /* 21 ! */ UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_6,
    /* 22 " */ UNICODE_DOT_5,
    /* 23 # */ UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 24 $ */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_4 | UNICODE_DOT_6,
    /* 25 % */ UNICODE_DOT_1 | UNICODE_DOT_4 | UNICODE_DOT_6,
    /* 26 & */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_6,
    /* 27 ' */ UNICODE_DOT_3,
    /* 28 ( */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 29 ) */ UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 2A * */ UNICODE_DOT_1 | UNICODE_DOT_6,
    /* 2B + */ UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_6,
    /* 2C , */ UNICODE_DOT_6,
    /* 2D - */ UNICODE_DOT_3 | UNICODE_DOT_6,
    /* 2E . */ UNICODE_DOT_4 | UNICODE_DOT_6,
    /* 2F / */ UNICODE_DOT_3 | UNICODE_DOT_4,
    /* 30 0 */ UNICODE_DOT_3 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 31 1 */ UNICODE_DOT_2,
    /* 32 2 */ UNICODE_DOT_2 | UNICODE_DOT_3,
    /* 33 3 */ UNICODE_DOT_2 | UNICODE_DOT_5,
    /* 34 4 */ UNICODE_DOT_2 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 35 5 */ UNICODE_DOT_2 | UNICODE_DOT_6,
    /* 36 6 */ UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_5,
    /* 37 7 */ UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 38 8 */ UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_6,
    /* 39 9 */ UNICODE_DOT_3 | UNICODE_DOT_5,
    /* 3A : */ UNICODE_DOT_1 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 3B ; */ UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 3C < */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_6,
    /* 3D = */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 3E > */ UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_5,
    /* 3F ? */ UNICODE_DOT_1 | UNICODE_DOT_4 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 40 @ */ UNICODE_DOT_4,
    /* 41 A */ UNICODE_DOT_1,
    /* 42 B */ UNICODE_DOT_1 | UNICODE_DOT_2,
    /* 43 C */ UNICODE_DOT_1 | UNICODE_DOT_4,
    /* 44 D */ UNICODE_DOT_1 | UNICODE_DOT_4 | UNICODE_DOT_5,
    /* 45 E */ UNICODE_DOT_1 | UNICODE_DOT_5,
    /* 46 F */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_4,
    /* 47 G */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_4 | UNICODE_DOT_5,
    /* 48 H */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_5,
    /* 49 I */ UNICODE_DOT_2 | UNICODE_DOT_4,
    /* 4A J */ UNICODE_DOT_2 | UNICODE_DOT_4 | UNICODE_DOT_5,
    /* 4B K */ UNICODE_DOT_1 | UNICODE_DOT_3,
    /* 4C L */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_3,
    /* 4D M */ UNICODE_DOT_1 | UNICODE_DOT_3 | UNICODE_DOT_4,
    /* 4E N */ UNICODE_DOT_1 | UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_5,
    /* 4F O */ UNICODE_DOT_1 | UNICODE_DOT_3 | UNICODE_DOT_5,
    /* 50 P */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_4,
    /* 51 Q */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_5,
    /* 52 R */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_5,
    /* 53 S */ UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_4,
    /* 54 T */ UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_5,
    /* 55 U */ UNICODE_DOT_1 | UNICODE_DOT_3 | UNICODE_DOT_6,
    /* 56 V */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_6,
    /* 57 W */ UNICODE_DOT_2 | UNICODE_DOT_4 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 58 X */ UNICODE_DOT_1 | UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_6,
    /* 59 Y */ UNICODE_DOT_1 | UNICODE_DOT_3 | UNICODE_DOT_4 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 5A Z */ UNICODE_DOT_1 | UNICODE_DOT_3 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 5B [ */ UNICODE_DOT_2 | UNICODE_DOT_4 | UNICODE_DOT_6,
    /* 5C \ */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 5D ] */ UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_4 | UNICODE_DOT_5 | UNICODE_DOT_6,
    /* 5E ^ */ UNICODE_DOT_4 | UNICODE_DOT_5,
    /* 5F _ */ UNICODE_DOT_4 | UNICODE_DOT_5 | UNICODE_DOT_6
  };

  private final static int symbolToIndex (byte symbol) {
    if (!isSymbol(symbol)) return -1;
    if ((symbol & ASCII_LETTER) != 0) symbol &= ~ASCII_LOWERCASE;
    return symbol - ASCII_MINIMUM;
  }

  public final static char symbolToCell (byte symbol) {
    int index = symbolToIndex(symbol);
    if (index < 0) return 0;
    return toCharacter(symbolToDots[index]);
  }

  public final static char symbolToText (byte symbol) {
    if (!isSymbol(symbol)) return 0;

    if ((symbol & ASCII_LETTER) != 0) {
      byte withoutDot7 = (byte)(symbol | ASCII_LOWERCASE);
      if (withoutDot7 <= ASCII_MAXIMUM) symbol = withoutDot7;
    }

    return (char)(symbol & 0XFF);
  }

  private final static byte[] dotsToSymbol = new byte[0X40];

  static {
    int count = symbolToDots.length;

    for (byte index=0; index<count; index+=1) {
      dotsToSymbol[symbolToDots[index]] = index;
    }
  }

  public final static byte charToSymbol (char character) {
    if (isBraillePattern(character)) {
      return (byte)(dotsToSymbol[character & 0X3F] + ASCII_MINIMUM);
    }

    if ((character >= ASCII_MINIMUM) && (character <= ASCII_MAXIMUM)) {
      return (byte)character;
    }

    return 0;
  }

  protected ASCIIBraille () {
    super();
  }
}

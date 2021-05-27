package org.nbp.common;

import android.util.Log;

public abstract class Braille {
  private final static String LOG_TAG = Braille.class.getName();

  protected Braille () {
  }

  public final static char UNICODE_ROW   = 0X2800;
  public final static char UNICODE_DOT_1 = 0X0001;
  public final static char UNICODE_DOT_2 = 0X0002;
  public final static char UNICODE_DOT_3 = 0X0004;
  public final static char UNICODE_DOT_4 = 0X0008;
  public final static char UNICODE_DOT_5 = 0X0010;
  public final static char UNICODE_DOT_6 = 0X0020;
  public final static char UNICODE_DOT_7 = 0X0040;
  public final static char UNICODE_DOT_8 = 0X0080;

  public final static char UNICODE_DOTS_ALL =
    UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_4
  | UNICODE_DOT_5 | UNICODE_DOT_6 | UNICODE_DOT_7 | UNICODE_DOT_8
  ;

  public final static boolean isBraillePattern (char character) {
    Character.UnicodeBlock block = Character.UnicodeBlock.of(character);
    if (block == null) return false;
    return block.equals(Character.UnicodeBlock.BRAILLE_PATTERNS);
  }

  public final static Byte toCell (char character) {
    if (isBraillePattern(character)) return (byte)(character & UNICODE_DOTS_ALL);
    return null;
  }

  public final static byte CELL_DOT_1 = (byte)UNICODE_DOT_1;
  public final static byte CELL_DOT_2 = (byte)UNICODE_DOT_2;
  public final static byte CELL_DOT_3 = (byte)UNICODE_DOT_3;
  public final static byte CELL_DOT_4 = (byte)UNICODE_DOT_4;
  public final static byte CELL_DOT_5 = (byte)UNICODE_DOT_5;
  public final static byte CELL_DOT_6 = (byte)UNICODE_DOT_6;
  public final static byte CELL_DOT_7 = (byte)UNICODE_DOT_7;
  public final static byte CELL_DOT_8 = (byte)UNICODE_DOT_8;

  public final static char toCharacter (byte cell) {
    char character = UNICODE_ROW;
    character |= cell & UNICODE_DOTS_ALL;
    return character;
  }

  public final static char[] toCharacters (byte[] cells) {
    if (cells == null) return null;

    int count = cells.length;
    char[] characters = new char[count];

    for (int index=0; index<count; index+=1) {
      characters[index] = toCharacter(cells[index]);
    }

    return characters;
  }

  public final static String toString (byte[] cells) {
    return new String(toCharacters(cells));
  }

  public static Byte parseDotNumbers (String numbers) {
    if (numbers.isEmpty()) {
      Log.w(LOG_TAG, "missing dot number(s)");
      return null;
    }

    byte dots = 0;
    int length = numbers.length();

    for (int index=0; index<length; index+=1) {
      final char number = numbers.charAt(index);
      final int dot;

      switch (number) {
        case '1': dot = CELL_DOT_1; break;
        case '2': dot = CELL_DOT_2; break;
        case '3': dot = CELL_DOT_3; break;
        case '4': dot = CELL_DOT_4; break;
        case '5': dot = CELL_DOT_5; break;
        case '6': dot = CELL_DOT_6; break;
        case '7': dot = CELL_DOT_7; break;
        case '8': dot = CELL_DOT_8; break;

        default:
          Log.w(LOG_TAG, ("unknown dot number: " + number));
          return null;
      }

      if ((dots & dot) != 0) {
        Log.w(LOG_TAG, ("dot number specified more than once: " + number));
        return null;
      }

      dots |= dot;
    }

    return dots;
  }
}

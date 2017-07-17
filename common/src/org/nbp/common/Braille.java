package org.nbp.common;

public abstract class Braille {
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

  public final static char DOTS_ALL =
    UNICODE_DOT_1 | UNICODE_DOT_2 | UNICODE_DOT_3 | UNICODE_DOT_4
  | UNICODE_DOT_5 | UNICODE_DOT_6 | UNICODE_DOT_7 | UNICODE_DOT_8
  ;

  public final static boolean isBraillePattern (char character) {
    Character.UnicodeBlock block = Character.UnicodeBlock.of(character);
    if (block == null) return false;
    return block.equals(Character.UnicodeBlock.BRAILLE_PATTERNS);
  }

  public final static Byte toCell (char character) {
    if (isBraillePattern(character)) return (byte)(character & DOTS_ALL);
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
    character |= cell & DOTS_ALL;
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
}

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

  public final static boolean isBraillePattern (char character) {
    Character.UnicodeBlock block = Character.UnicodeBlock.of(character);
    if (block == null) return false;
    return block.equals(Character.UnicodeBlock.BRAILLE_PATTERNS);
  }
}

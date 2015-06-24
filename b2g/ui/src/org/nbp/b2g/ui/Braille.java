package org.nbp.b2g.ui;

public abstract class Braille {
  public final static char UNICODE_ROW = 0X2800;
  public final static char DOT_1       = 0X0001;
  public final static char DOT_2       = 0X0002;
  public final static char DOT_3       = 0X0004;
  public final static char DOT_4       = 0X0008;
  public final static char DOT_5       = 0X0010;
  public final static char DOT_6       = 0X0020;
  public final static char DOT_7       = 0X0040;
  public final static char DOT_8       = 0X0080;

  public static char toCharacter (byte cell) {
    char character = UNICODE_ROW;

    if ((cell & BrailleDevice.DOT_1) != 0) character |= DOT_1;
    if ((cell & BrailleDevice.DOT_2) != 0) character |= DOT_2;
    if ((cell & BrailleDevice.DOT_3) != 0) character |= DOT_3;
    if ((cell & BrailleDevice.DOT_4) != 0) character |= DOT_4;
    if ((cell & BrailleDevice.DOT_5) != 0) character |= DOT_5;
    if ((cell & BrailleDevice.DOT_6) != 0) character |= DOT_6;
    if ((cell & BrailleDevice.DOT_7) != 0) character |= DOT_7;
    if ((cell & BrailleDevice.DOT_8) != 0) character |= DOT_8;

    return character;
  }

  public static char[] toCharacters (byte[] cells) {
    if (cells == null) return null;

    int count = cells.length;
    char[] characters = new char[count];

    for (int index=0; index<count; index+=1) {
      characters[index] = toCharacter(cells[index]);
    }

    return characters;
  }

  public static String toString (byte[] cells) {
    return new String(toCharacters(cells));
  }

  public static void setCells (byte[] cells, String text, Characters characters) {
    int count = Math.min(text.length(), cells.length);
    int index = 0;

    while (index < count) {
      char character = text.charAt(index);
      Byte dots = characters.getDots(character);
      cells[index++] = (dots != null)? dots: ApplicationParameters.BRAILLE_CHARACTER_UNDEFINED;
    }

    while (index < cells.length) cells[index++] = 0;
  }

  private Braille () {
  }
}

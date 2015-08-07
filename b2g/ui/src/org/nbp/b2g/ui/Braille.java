package org.nbp.b2g.ui;

public abstract class Braille {
  public final static char UNICODE_ROW   = 0X2800;
  public final static char UNICODE_DOT_1 = 0X0001;
  public final static char UNICODE_DOT_2 = 0X0002;
  public final static char UNICODE_DOT_3 = 0X0004;
  public final static char UNICODE_DOT_4 = 0X0008;
  public final static char UNICODE_DOT_5 = 0X0010;
  public final static char UNICODE_DOT_6 = 0X0020;
  public final static char UNICODE_DOT_7 = 0X0040;
  public final static char UNICODE_DOT_8 = 0X0080;

  public static char toCharacter (byte cell) {
    char character = UNICODE_ROW;

    if ((cell & BrailleDevice.DOT_1) != 0) character |= UNICODE_DOT_1;
    if ((cell & BrailleDevice.DOT_2) != 0) character |= UNICODE_DOT_2;
    if ((cell & BrailleDevice.DOT_3) != 0) character |= UNICODE_DOT_3;
    if ((cell & BrailleDevice.DOT_4) != 0) character |= UNICODE_DOT_4;
    if ((cell & BrailleDevice.DOT_5) != 0) character |= UNICODE_DOT_5;
    if ((cell & BrailleDevice.DOT_6) != 0) character |= UNICODE_DOT_6;
    if ((cell & BrailleDevice.DOT_7) != 0) character |= UNICODE_DOT_7;
    if ((cell & BrailleDevice.DOT_8) != 0) character |= UNICODE_DOT_8;

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

  public static void clearCells (byte[] cells, int from) {
    while (from < cells.length) cells[from++] = 0;
  }

  public static void clearCells (byte[] cells) {
    clearCells(cells, 0);
  }

  public static String setCells (byte[] cells, String text) {
    Characters characters = Characters.getCharacters();
    int count = Math.min(text.length(), cells.length);
    text = text.substring(0, count);
    int index = 0;

    while (index < count) {
      char character = text.charAt(index);
      Byte dots = characters.toDots(character);
      cells[index++] = (dots != null)? dots: ApplicationParameters.BRAILLE_CHARACTER_UNDEFINED;
    }

    clearCells(cells, index);
    return text;
  }

  public static String setCells (byte[] cells, Endpoint endpoint) {
    synchronized (endpoint) {
      String text = endpoint.getLineText();
      int length = text.length();

      int indent = endpoint.getLineIndent();
      if (indent > length) indent = length;
      text = setCells(cells, text.substring(indent));

      if (endpoint.isInputArea()) {
        int start = endpoint.getSelectionStart();
        int end = endpoint.getSelectionEnd();

        if (endpoint.isSelected(start) && endpoint.isSelected(end)) {
          int brailleStart = endpoint.getBrailleStart();
          int nextLine = length - indent + 1;

          if ((start -= brailleStart) < 0) start = 0;
          if ((end -= brailleStart) > nextLine) end = nextLine;

          if (start == end) {
            if (end < cells.length) {
              cells[end] |= ApplicationSettings.CURSOR_INDICATOR.getDots();
            }
          } else {
            if (end > cells.length) end = cells.length;

            while (start < end) {
              cells[start++] |= ApplicationSettings.SELECTION_INDICATOR.getDots();
            }
          }
        }
      }

      return text;
    }
  }

  public static String setCells (byte[] cells) {
    return setCells(cells, Endpoints.getCurrentEndpoint());
  }

  private Braille () {
  }
}

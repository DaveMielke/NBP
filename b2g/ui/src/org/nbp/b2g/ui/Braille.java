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

  public static CharSequence setCells (byte[] cells, CharSequence text) {
    int count = Math.min(text.length(), cells.length);
    text = text.subSequence(0, count);

    Characters characters = Characters.getCharacters();
    int index = 0;

    while (index < count) {
      char character = text.charAt(index);
      Byte dots = characters.toDots(character);
      cells[index++] = (dots != null)? dots: ApplicationParameters.BRAILLE_CHARACTER_UNDEFINED;
    }

    clearCells(cells, index);
    return text;
  }

  public static CharSequence setCells (byte[] cells, Endpoint endpoint) {
    synchronized (endpoint) {
      CharSequence text = endpoint.getLineText();
      int length = text.length();

      int indent = endpoint.getLineIndent();
      if (indent > length) indent = length;
      text = setCells(cells, text.subSequence(indent, text.length()));

      if (endpoint.isInputArea()) {
        int from = endpoint.getSelectionStart();
        int to = endpoint.getSelectionEnd();

        if (endpoint.isSelected(from) && endpoint.isSelected(to)) {
          int brailleStart = endpoint.getBrailleStart();
          int end = length - indent;

          from -= brailleStart;
          to -= brailleStart;

          if (from == to) {
            if ((to >= 0) && (to <= end)) {
              if (to < cells.length) {
                cells[to] |= ApplicationSettings.CURSOR_INDICATOR.getDots();
              }
            }
          } else {
            if (from < 0) from = 0;
            if (to > end) to = end;
            if (to > cells.length) to = cells.length;

            while (from < to) {
              cells[from++] |= ApplicationSettings.SELECTION_INDICATOR.getDots();
            }
          }
        }
      }

      return text;
    }
  }

  public static CharSequence setCells (byte[] cells) {
    return setCells(cells, Endpoints.getCurrentEndpoint());
  }

  private Braille () {
  }
}

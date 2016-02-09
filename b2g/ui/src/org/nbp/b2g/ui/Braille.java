package org.nbp.b2g.ui;

import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

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

  public static int setCells (byte[] cells, CharSequence text) {
    final Characters characters = Characters.getCharacters();
    final int count = Math.min(text.length(), cells.length);
    int index = 0;

    while (index < count) {
      char character = text.charAt(index);
      Byte dots = characters.toDots(character);
      cells[index++] = (dots != null)? dots: ApplicationParameters.BRAILLE_CHARACTER_UNDEFINED;
    }

    clearCells(cells, index);
    return count;
  }

  private static void markCells (
    byte[] cells, Endpoint endpoint, int from, int to, int indent
  ) {
    from = endpoint.findFirstBrailleOffset(from);
    to = endpoint.findEndBrailleOffset(to);

    if ((from -= indent) < 0) from = 0;
    if ((to -= indent) > cells.length) to = cells.length;

    while (from < to) {
      cells[from++] |= ApplicationSettings.SELECTION_INDICATOR.getDots();
    }
  }

  public static CharSequence setCells (byte[] cells, Endpoint endpoint) {
    synchronized (endpoint) {
      CharSequence lineText = endpoint.getLineText();
      int lineLength = lineText.length();

      int lineIndent = endpoint.getLineIndent();
      if (lineIndent > lineLength) lineIndent = lineLength;

      CharSequence braille = endpoint.getBrailleCharacters();
      int brailleIndent = endpoint.findFirstBrailleOffset(lineIndent);
      braille = braille.subSequence(brailleIndent, braille.length());

      int cellCount = setCells(cells, braille);
      int lineEnd = endpoint.getAdjustedLineOffset(cellCount, lineIndent);
      CharSequence text = lineText.subSequence(lineIndent, lineEnd);

      if (endpoint.isInputArea()) {
        boolean hasSelection = false;

        int from = endpoint.getSelectionStart();
        int to = endpoint.getSelectionEnd();

        if (Endpoint.isSelected(from) && endpoint.isSelected(to)) {
          {
            int start = endpoint.getLineStart();
            from -= start;
            to -= start;
          }

          if (from == to) {
            if ((to >= 0) && (to <= lineLength)) {
              to = endpoint.getBrailleOffset(to);
              to -= brailleIndent;

              if ((to >= 0) && (to < cells.length)) {
                cells[to] |= ApplicationSettings.CURSOR_INDICATOR.getDots();
              }
            }
          } else {
            hasSelection = true;
            if (from < 0) from = 0;
            if (to > lineLength) to = lineLength;
            markCells(cells, endpoint, from, to, brailleIndent);
          }
        }

        if (!hasSelection) {
          if (lineText instanceof Spanned) {
            Spanned spanned = (Spanned)lineText;
            CharacterStyle[] spans = spanned.getSpans(0, lineText.length(), CharacterStyle.class);

            if (spans != null) {
              for (CharacterStyle span : spans) {
                if (span instanceof StyleSpan) {
                } else if (span instanceof UnderlineSpan) {
                } else {
                  continue;
                }

                int start = spanned.getSpanStart(span);
                int end = spanned.getSpanEnd(span);
                markCells(cells, endpoint, start, end, brailleIndent);
              }
            }
          }
        }
      }

      return text;
    }
  }

  private Braille () {
  }
}

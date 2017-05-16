package org.nbp.b2g.ui;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;
import android.text.Spanned;

import org.nbp.common.Braille;

public abstract class BrailleUtilities {
  public static char toCharacter (byte cell) {
    char character = Braille.UNICODE_ROW;

    if ((cell & BrailleDevice.DOT_1) != 0) character |= Braille.UNICODE_DOT_1;
    if ((cell & BrailleDevice.DOT_2) != 0) character |= Braille.UNICODE_DOT_2;
    if ((cell & BrailleDevice.DOT_3) != 0) character |= Braille.UNICODE_DOT_3;
    if ((cell & BrailleDevice.DOT_4) != 0) character |= Braille.UNICODE_DOT_4;
    if ((cell & BrailleDevice.DOT_5) != 0) character |= Braille.UNICODE_DOT_5;
    if ((cell & BrailleDevice.DOT_6) != 0) character |= Braille.UNICODE_DOT_6;
    if ((cell & BrailleDevice.DOT_7) != 0) character |= Braille.UNICODE_DOT_7;
    if ((cell & BrailleDevice.DOT_8) != 0) character |= Braille.UNICODE_DOT_8;

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
    byte[] cells, Endpoint endpoint,
    int from, int to,
    int indent, int count
  ) {
    from = endpoint.findFirstBrailleOffset(from);
    to = endpoint.findEndBrailleOffset(to);

    if ((from -= indent) < 0) from = 0;
    if ((to -= indent) > count) to = count;

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

      int lineEnd = endpoint.findNextSegment(cells.length, lineIndent);
      CharSequence text = lineText.subSequence(lineIndent, lineEnd);

      int brailleIndent = endpoint.findFirstBrailleOffset(lineIndent);
      CharSequence braille = endpoint.getBrailleCharacters().subSequence(
        brailleIndent,
        endpoint.findFirstBrailleOffset(lineEnd)
      );
      int cellCount = setCells(cells, braille);

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
              boolean atEnd = to == lineLength;

              to = endpoint.getBrailleOffset(to);
              to -= brailleIndent;

              if ((to >= 0) && (to < cells.length)) {
                if ((to < cellCount) || ((to == cellCount) && atEnd)) {
                  cells[to] |= ApplicationSettings.CURSOR_INDICATOR.getDots();
                }
              }
            }
          } else {
            hasSelection = true;
            if (from < 0) from = 0;
            if (to > lineLength) to = lineLength;

            if (from < to) {
              markCells(cells, endpoint, from, to, brailleIndent, cellCount);
            }
          }
        }

        if (!hasSelection && ApplicationSettings.SHOW_HIGHLIGHTED) {
          if (lineText instanceof Spanned) {
            Spanned spanned = (Spanned)lineText;
            Object[] spans = spanned.getSpans(0, lineLength, Object.class);

            if (spans != null) {
              for (Object span : spans) {
                if (!(span instanceof CharacterStyle)) continue;
                if (HighlightSpans.getEntry((CharacterStyle)span) == null) continue;

                int start = spanned.getSpanStart(span);
                int end = spanned.getSpanEnd(span);
                markCells(cells, endpoint, start, end, brailleIndent, cellCount);
              }
            }
          }
        }
      }

      return text;
    }
  }

  private BrailleUtilities () {
  }
}

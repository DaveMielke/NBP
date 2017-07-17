package org.nbp.b2g.ui;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;
import android.text.Spanned;

import org.nbp.common.Braille;

public abstract class BrailleUtilities {
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

  private final static char[] internalDots = new char[] {
    Braille.UNICODE_DOT_1,
    Braille.UNICODE_DOT_2,
    Braille.UNICODE_DOT_3,
    Braille.UNICODE_DOT_4,
    Braille.UNICODE_DOT_5,
    Braille.UNICODE_DOT_6,
    Braille.UNICODE_DOT_7,
    Braille.UNICODE_DOT_8
  };

  public final static byte[] makeTranslationTable (
    int dot1, int dot2, int dot3, int dot4,
    int dot5, int dot6, int dot7, int dot8
  ) {
    int dotCount = internalDots.length;

    int[] externalDots = new int[] {
      dot1, dot2, dot3, dot4, dot5, dot6, dot7, dot8
    };

    if (externalDots.length != dotCount) {
      throw new RuntimeException("array length mismatch");
    }

    {
      boolean sameLayout = true;
      int externalCell = 0;
      int dotIndex = 0;

      while (dotIndex < dotCount) {
        char internalDot = internalDots[dotIndex];
        int externalDot = externalDots[dotIndex];

        if (externalDot == 0) break;
        if ((Braille.UNICODE_DOTS_ALL & externalDot) != externalDot) break;

        if ((externalCell & externalDot) != 0) break;
        externalCell |= externalDot;

        if (externalDot != internalDot) sameLayout = false;
        dotIndex += 1;
      }

      if (dotIndex != dotCount) {
        throw new IllegalArgumentException(String.format(
          "dot%d is 0X%02X", (dotIndex + 1), externalDots[dotIndex]
        ));
      }

      if (sameLayout) return null;
    }

    final int tableSize = 0X100;
    byte[] table = new byte[tableSize];

    for (int internalCell=0; internalCell<tableSize; internalCell+=1) {
      byte externalCell = 0;

      for (int dotIndex=0; dotIndex<dotCount; dotIndex+=1) {
        if ((internalCell & internalDots[dotIndex]) != 0) {
          externalCell |= externalDots[dotIndex];
        }
      }

      table[internalCell] = externalCell;
    }

    return table;
  }

  private BrailleUtilities () {
  }
}

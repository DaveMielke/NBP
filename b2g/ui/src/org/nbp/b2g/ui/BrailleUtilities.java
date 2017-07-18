package org.nbp.b2g.ui;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;
import android.text.Spanned;

import org.nbp.common.Braille;

public abstract class BrailleUtilities {
  private BrailleUtilities () {
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
    char allDots = Braille.UNICODE_DOTS_ALL;

    int[] externalDots = new int[] {
      dot1, dot2, dot3, dot4, dot5, dot6, dot7, dot8
    };

    {
      boolean same = true;
      int cell = 0;

      int count = externalDots.length;
      int index = 0;

      while (index < count) {
        int dot = externalDots[index];
        if (dot != internalDots[index]) same = false;

        if (dot == 0) break;
        if ((allDots & dot) != dot) break;

        if ((cell & dot) != 0) break;
        cell |= dot;

        index += 1;
      }

      if (index != count) {
        throw new IllegalArgumentException(String.format(
          "dot %d is 0X%02X", (index + 1), externalDots[index]
        ));
      }

      if (same) return null;
    }

    int dotCount = internalDots.length;
    byte[] table = new byte[allDots + 1];

    for (int internalCell=0; internalCell<=allDots; internalCell+=1) {
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

  public final static void translateCells (byte[] table, byte[] from, byte[] to) {
    int count = from.length;

    if (to.length != count) {
      throw new IllegalArgumentException("cell count mismatch");
    }

    if (table != null) {
      for (int index=0; index<count; index+=1) {
        to[index] = table[from[index] & Braille.UNICODE_DOTS_ALL];
      }
    } else if (to != from) {
      System.arraycopy(from, 0, to, 0, count);
    }
  }

  public final static byte[] translateCells (byte[] table, byte[] from) {
    if (table == null) return from;

    byte[] to = new byte[from.length];
    translateCells(table, from, to);
    return to;
  }
}

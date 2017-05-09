package org.nbp.editor;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import android.text.Spanned;
import android.text.Spannable;

import android.util.Log;

public abstract class Spans {
  private final static String LOG_TAG = Spans.class.getName();

  private final static char ESCAPE = '\\';

  private Spans () {
  }

  private abstract static class SpanEntry {
    protected SpanEntry () {
    }

    public abstract String getIdentifier ();
    public abstract Class<?> getType ();
    protected abstract String[] getPropertyNames ();

    public final int getPropertyCount () {
      String[] names = getPropertyNames();
      if (names == null) return 0;
      return names.length;
    }

    public final String getPropertyName (int index) {
      return getPropertyNames()[index];
    }

    public abstract String getPropertyValue (Object span, int index);
  }

  private final static Map<String, SpanEntry> spanIdentifiers =
               new HashMap<String, SpanEntry>();

  private final static Map<Class<?>, SpanEntry> spanTypes =
               new HashMap<Class<?>, SpanEntry>();

  private final static void addSpanEntry (SpanEntry entry) {
    String identifier = entry.getIdentifier();
    Class<?> type = entry.getType();

    spanIdentifiers.put(identifier, entry);
    spanTypes.put(type, entry);
  }

  private final static void addSimpleSpanEntry (
    final String identifier,
    final Class<?> type
  ) {
    addSpanEntry(
      new SpanEntry () {
        @Override
        public final String getIdentifier () {
          return identifier;
        }

        @Override
        public final Class<?> getType () {
          return type;
        }

        @Override
        protected final String[] getPropertyNames () {
          return null;
        }

        @Override
        public final String getPropertyValue (Object span, int index) {
          return null;
        }
      }
    );
  }

  private final static void addRevisionSpanEntry (
    final String identifier,
    final Class<? extends RevisionSpan> type
  ) {
    addSpanEntry(
      new SpanEntry () {
        @Override
        public final String getIdentifier () {
          return identifier;
        }

        @Override
        public final Class<?> getType () {
          return type;
        }

        private String[] properties = new String[] {"author", "time"};

        @Override
        protected final String[] getPropertyNames () {
          return properties;
        }

        @Override
        public final String getPropertyValue (Object span, int index) {
          RevisionSpan revision = (RevisionSpan)span;

          switch (index) {
            case 0:
              return revision.getAuthor();

            case 1: {
              Date timestamp = revision.getTimestamp();
              if (timestamp == null) break;
              return Long.toString(timestamp.getTime());
            }
          }

          return null;
        }
      }
    );
  }

  static {
    addSimpleSpanEntry("dec", DecorationSpan.class);
    addSimpleSpanEntry("sec", SectionSpan.class);
    addSimpleSpanEntry("par", ParagraphSpan.class);
    addSimpleSpanEntry("com", CommentSpan.class);

    addRevisionSpanEntry("ins", InsertSpan.class);
    addRevisionSpanEntry("del", DeleteSpan.class);

    addSpanEntry(
      new SpanEntry () {
        @Override
        public final String getIdentifier () {
          return "fgc";
        }

        @Override
        public final Class<?> getType () {
          return ForegroundColorSpan.class;
        }

        private String[] properties = new String[] {"color"};

        @Override
        protected final String[] getPropertyNames () {
          return properties;
        }

        @Override
        public final String getPropertyValue (Object span, int index) {
          ForegroundColorSpan fgc = (ForegroundColorSpan)span;

          switch (index) {
            case 0:
              return Integer.toString(fgc.getForegroundColor());
          }

          return null;
        }
      }
    );
  }

  public final static String saveSpans (CharSequence text) {
    if (text == null) return null;
    if (!(text instanceof Spanned)) return null;
    Spanned spanned = (Spanned)text;

    Object[] spans = spanned.getSpans(0, spanned.length(), Object.class);
    if (spans == null) return null;
    StringBuilder sb = new StringBuilder();

    for (Object span : spans) {
      SpanEntry entry = null;
      String identifier = null;

      if (span instanceof CharacterStyle) {
        CharacterStyle style = (CharacterStyle)span;
        HighlightSpans.Entry highlight = HighlightSpans.getEntry(style);
        if (highlight != null) identifier = highlight.getIdentifier();
      }

      if (identifier == null) {
        entry = spanTypes.get(span.getClass());
        if (entry != null) identifier = entry.getIdentifier();
      }

      if (identifier == null) continue;
      if (sb.length() > 0) sb.append(' ');
      sb.append(identifier);

      if (entry != null) {
        int count = entry.getPropertyCount();

        for (int index=0; index<count; index+=1) {
          String value = entry.getPropertyValue(span, index);
          if (value == null) continue;
          if (value.isEmpty()) continue;

          sb.append(" -");
          sb.append(entry.getPropertyName(index));
          sb.append(' ');

          int length = value.length();
          char[] characters = new char[length];
          value.getChars(0, length, characters, 0);

          for (char character : characters) {
            if (Character.isWhitespace(character) ||
                Character.isISOControl(character)) {
              sb.append(String.format("%cu%04X", ESCAPE, (int)character));
            } else {
              if (character == ESCAPE) sb.append(ESCAPE);
              sb.append(character);
            }
          }
        }
      }

      sb.append(' ');
      sb.append(spanned.getSpanStart(span));

      sb.append(' ');
      sb.append(spanned.getSpanEnd(span));

      sb.append(' ');
      sb.append(spanned.getSpanFlags(span));
    }

    if (sb.length() == 0) return null;
    return sb.toString();
  }

  private final static Object restoreSpan (String identifier) {
    {
      HighlightSpans.Entry spanEntry = HighlightSpans.getEntry(identifier);
      if (spanEntry != null) return spanEntry.newInstance();
    }

    return null;
  }

  public final static void restoreSpans (Spannable text, String[] fields) {
    int length = text.length();
    int count = fields.length;
    int index = 0;

    while (index < count) {
      String identifier;
      int start;
      int end;
      int flags;

      try {
        identifier = fields[index++];
        start = Integer.valueOf(fields[index++]);
        end = Integer.valueOf(fields[index++]);
        flags = Integer.valueOf(fields[index++]);
      } catch (Exception exception) {
        Log.w(LOG_TAG, ("span restoration error: " + exception.getMessage()));
        break;
      }

      if (ApplicationUtilities.verifyTextRange(start, end, length)) {
        Object span = restoreSpan(identifier);
        if (span == null) continue;
        text.setSpan(span, start, end, flags);
      }
    }
  }
}

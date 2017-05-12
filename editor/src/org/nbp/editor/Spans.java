package org.nbp.editor;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import java.util.Properties;
import java.io.StringReader;
import java.io.IOException;

import org.nbp.common.LanguageUtilities;
import java.lang.reflect.Constructor;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import android.text.Spanned;
import android.text.Spannable;

import android.util.Log;

public abstract class Spans {
  private final static String LOG_TAG = Spans.class.getName();

  private Spans () {
  }

  private final static char ESCAPE_CHARACTER = '\\';
  private final static String PROPERTY_PREFIX = "-";

  private final static String parseEscapedString (String string) {
    Properties properties = new Properties();
    String key = "x";

    try {
      properties.load(new StringReader((key + '=' + string)));
      return properties.getProperty(key);
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("escaped string load error: " + string));
    }

    return string;
  }

  private abstract static class SpanEntry {
    protected SpanEntry () {
    }

    public abstract String getIdentifier ();
    public abstract Class<?> getType ();
    protected abstract String[] getPropertyNames ();
    public abstract String getPropertyValue (Object span, int index);
    public abstract Object newSpan (String[] properties);

    public final int getPropertyCount () {
      String[] names = getPropertyNames();
      if (names == null) return 0;
      return names.length;
    }

    public final String getPropertyName (int index) {
      return getPropertyNames()[index];
    }
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

        @Override
        public final Object newSpan (String[] properties) {
          Constructor constructor = LanguageUtilities.getConstructor(getType());
          if (constructor == null) return null;
          return LanguageUtilities.newInstance(constructor);
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

        @Override
        public final Object newSpan (String[] properties) {
          Constructor constructor = LanguageUtilities.getConstructor(getType());
          if (constructor == null) return null;

          RevisionSpan span = (RevisionSpan)LanguageUtilities.newInstance(constructor);
          if (span == null) return null;

          String author = properties[0];
          String timestamp = properties[1];

          if (author != null) span.setAuthor(parseEscapedString(author));
          if (timestamp != null) span.setTimestamp(new Date(Long.valueOf(timestamp)));

          return span;
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

        @Override
        public final Object newSpan (String[] properties) {
          Constructor constructor = LanguageUtilities.getConstructor(getType(), Integer.TYPE);
          if (constructor == null) return null;
          return LanguageUtilities.newInstance(constructor, Integer.valueOf(properties[0]));
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

          sb.append(' ');
          sb.append(PROPERTY_PREFIX);
          sb.append(entry.getPropertyName(index));
          sb.append(' ');

          int length = value.length();
          char[] characters = new char[length];
          value.getChars(0, length, characters, 0);

          for (char character : characters) {
            if (Character.isWhitespace(character) ||
                Character.isISOControl(character)) {
              sb.append(String.format("%cu%04X", ESCAPE_CHARACTER, (int)character));
            } else {
              if (character == ESCAPE_CHARACTER) sb.append(ESCAPE_CHARACTER);
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

  private static class SpanProperties extends HashMap<String, String> {
    public SpanProperties () {
      super();
    }
  }

  private final static int parseProperties (SpanProperties properties, String[] fields, int index) {
    while (index < fields.length) {
      String name = fields[index];
      if (!name.startsWith(PROPERTY_PREFIX)) break;
      name = name.substring(PROPERTY_PREFIX.length());

      if (++index == fields.length) {
        Log.w(LOG_TAG, "missing property value");
        break;
      }

      String value = fields[index++];

      if (name.isEmpty()) {
        Log.w(LOG_TAG, "missing property name");
        break;
      }

      if (properties.get(name) != null) {
        Log.w(LOG_TAG, ("property defined more than once: " + name));
        break;
      }

      properties.put(name, value);
    }

    return index;
  }

  private final static Object restoreSpan (String identifier, SpanProperties properties) {
    {
      HighlightSpans.Entry entry = HighlightSpans.getEntry(identifier);
      if (entry != null) return entry.newInstance();
    }

    SpanEntry entry = spanIdentifiers.get(identifier);
    if (entry == null) return null;

    String[] array = null;
    int count = entry.getPropertyCount();

    if (count > 0) {
      array = new String[count];

      for (int index=0; index<count; index+=1) {
        array[index] = properties.get(entry.getPropertyName(index));
      }
    }

    return entry.newSpan(array);
  }

  public final static boolean restoreSpans (Spannable text, String[] fields) {
    boolean added = false;
    int length = text.length();
    int count = fields.length;
    int index = 0;

    while (index < count) {
      String identifier = fields[index++];
      SpanProperties properties = new SpanProperties();
      index = parseProperties(properties, fields, index);

      int start;
      int end;
      int flags;

      try {
        start = Integer.valueOf(fields[index++]);
        end = Integer.valueOf(fields[index++]);
        flags = Integer.valueOf(fields[index++]);
      } catch (Exception exception) {
        Log.w(LOG_TAG, ("operand error: " + exception.getMessage()));
        break;
      }

      if (ApplicationUtilities.verifyTextRange(start, end, length)) {
        Object span = restoreSpan(identifier, properties);
        if (span == null) continue;

        text.setSpan(span, start, end, flags);
        added = true;

        if (span instanceof RegionSpan) {
          RegionSpan region = (RegionSpan)span;
          region.restoreData(text);
        }
      }
    }

    return added;
  }

  public final static boolean restoreSpans (Spannable text, String spans) {
    if (spans == null) return false;

    spans = spans.trim();
    if (spans.isEmpty()) return false;

    String[] fields = spans.split("\\s+");
    return restoreSpans(text, fields);
  }
}

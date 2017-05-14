package org.nbp.editor;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.nbp.common.LanguageUtilities;
import java.lang.reflect.Constructor;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import android.text.Spanned;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import android.util.Log;

public abstract class Spans {
  private final static String LOG_TAG = Spans.class.getName();

  private Spans () {
  }

  private final static String PROPERTY_PREFIX = "-";

  private final static void logInvalidSavedValue (String value, String what) {
    Log.w(LOG_TAG, String.format("invalid saved %s: %s", what, value));
  }

  private final static Integer newInteger (String string, String what) {
    if (string != null) {
      try {
        return Integer.valueOf(string);
      } catch (NumberFormatException exception) {
        logInvalidSavedValue(string, what);
      }
    }

    return null;
  }

  private final static Integer newColor (String string) {
    return newInteger(string, "color");
  }

  private final static Long newLong (String string, String what) {
    if (string != null) {
      try {
        return Long.valueOf(string);
      } catch (NumberFormatException exception) {
        logInvalidSavedValue(string, what);
      }
    }

    return null;
  }

  private final static Date newTimestamp (String string) {
    Long timestamp = newLong(string, "timestamp");
    if (timestamp == null) return null;
    return new Date(timestamp);
  }

  private final static Editable newContent (String text, String spans) {
    if (text == null) return null;
    Editable content = new SpannableStringBuilder(text);
    if (spans != null) Spans.restoreSpans(content, spans);
    return content;
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

        private String[] properties = new String[] {"name", "time"};

        @Override
        protected final String[] getPropertyNames () {
          return properties;
        }

        @Override
        public final String getPropertyValue (Object span, int index) {
          RevisionSpan revision = (RevisionSpan)span;

          switch (index) {
            case 0:
              return revision.getName();

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
          String name = properties[0];
          Date timestamp = newTimestamp(properties[1]);

          Constructor constructor = LanguageUtilities.getConstructor(getType());
          if (constructor == null) return null;

          RevisionSpan revision = (RevisionSpan)LanguageUtilities.newInstance(constructor);
          if (revision == null) return null;

          if (name != null) revision.setName(name);
          if (timestamp != null) revision.setTimestamp(timestamp);
          return revision;
        }
      }
    );
  }

  static {
    addSimpleSpanEntry("dec", DecorationSpan.class);
    addSimpleSpanEntry("sec", SectionSpan.class);
    addSimpleSpanEntry("par", ParagraphSpan.class);

    addRevisionSpanEntry("ins", InsertSpan.class);
    addRevisionSpanEntry("del", DeleteSpan.class);

    addSpanEntry(
      new SpanEntry () {
        @Override
        public final String getIdentifier () {
          return "com";
        }

        @Override
        public final Class<?> getType () {
          return CommentSpan.class;
        }

        private String[] properties = new String[] {"text", "spans", "name", "initials", "time"};

        @Override
        protected final String[] getPropertyNames () {
          return properties;
        }

        @Override
        public final String getPropertyValue (Object span, int index) {
          CommentSpan comment = (CommentSpan)span;

          switch (index) {
            case 0:
              return comment.getCommentText().toString();

            case 1:
              return Spans.saveSpans(comment.getCommentText());

            case 2:
              return comment.getName();

            case 3:
              return comment.getInitials();

            case 4:
              return Long.toString(comment.getTimestamp().getTime());
          }

          return null;
        }

        @Override
        public final Object newSpan (String[] properties) {
          Editable content = newContent(properties[0], properties[1]);
          if (content == null) return null;

          String name = properties[2];
          String initials = properties[3];
          Date timestamp = newTimestamp(properties[4]);

          Constructor constructor = LanguageUtilities.getConstructor(getType(), Editable.class);
          if (constructor == null) return null;

          CommentSpan comment = (CommentSpan)LanguageUtilities.newInstance(constructor, content);
          if (comment == null) return null;

          if (name != null) comment.setName(name);
          if (initials != null) comment.setInitials(initials);
          if (timestamp != null) comment.setTimestamp(timestamp);
          return comment;
        }
      }
    );

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
          Integer color = newColor(properties[0]);
          if (color == null) return null;

          Constructor constructor = LanguageUtilities.getConstructor(getType(), Integer.TYPE);
          if (constructor == null) return null;

          return LanguageUtilities.newInstance(constructor, color);
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
          sb.append(ApplicationUtilities.encodeString(value));
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

      properties.put(name, ApplicationUtilities.decodeString(value));
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

        if (span instanceof EditorSpan) {
          ((EditorSpan)span).restoreSpan(text);
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

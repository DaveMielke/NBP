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

  public final static void logSpans (CharSequence text, int start, int end) {
    StringBuilder sb = new StringBuilder("spans:");

    sb.append(' ');
    sb.append(text.getClass().getName());

    sb.append(' ');
    sb.append(start);
    sb.append("..");
    sb.append(end);

    sb.append(' ');
    sb.append('"');
    sb.append(text.subSequence(start, end));
    sb.append('"');

    if (text instanceof Spanned) {
      Spanned content = (Spanned)text;

      Object[] spans = content.getSpans(start, end, Object.class);
      sb.append(' ');
      sb.append(spans.length);

      for (Object span : spans) {
        sb.append(' ');
        sb.append(span.getClass().getName());

        sb.append(' ');
        sb.append(content.getSpanStart(span));
        sb.append("..");
        sb.append(content.getSpanEnd(span));
      }
    }

    Log.d(LOG_TAG, sb.toString());
  }

  public final static void logSpans (CharSequence text) {
    logSpans(text, 0, text.length());
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

  private final static Date newTime (String string) {
    Long time = newLong(string, "time");
    if (time == null) return null;
    return new Date(time);
  }

  private final static Editable newContent (String text, String spans) {
    if (text == null) return null;
    Editable content = new SpannableStringBuilder(text);
    if (spans != null) restoreSpans(content, spans);
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

  static {
    addSimpleSpanEntry("dec", DecorationSpan.class);
    addSimpleSpanEntry("sec", SectionSpan.class);
    addSimpleSpanEntry("par", ParagraphSpan.class);

    addSpanEntry(
      new SpanEntry () {
        @Override
        public final String getIdentifier () {
          return "ins";
        }

        @Override
        public final Class<?> getType () {
          return InsertionSpan.class;
        }

        private String[] properties = new String[] {"name", "time"};

        @Override
        protected final String[] getPropertyNames () {
          return properties;
        }

        @Override
        public final String getPropertyValue (Object span, int index) {
          InsertionSpan insertion = (InsertionSpan)span;

          switch (index) {
            case 0:
              return insertion.getReviewerName();

            case 1: {
              Date time = insertion.getReviewTime();
              if (time == null) break;
              return Long.toString(time.getTime());
            }
          }

          return null;
        }

        @Override
        public final Object newSpan (String[] properties) {
          String name = properties[0];
          Date time = newTime(properties[1]);

          Constructor constructor = LanguageUtilities.getConstructor(getType());
          if (constructor == null) return null;

          InsertionSpan insertion = (InsertionSpan)LanguageUtilities.newInstance(constructor);
          if (insertion == null) return null;

          if (name != null) insertion.setReviewerName(name);
          if (time != null) insertion.setReviewTime(time);
          return insertion;
        }
      }
    );

    addSpanEntry(
      new SpanEntry () {
        @Override
        public final String getIdentifier () {
          return "del";
        }

        @Override
        public final Class<?> getType () {
          return DeletionSpan.class;
        }

        private String[] properties = new String[] {"name", "time", "iname", "itime"};

        @Override
        protected final String[] getPropertyNames () {
          return properties;
        }

        @Override
        public final String getPropertyValue (Object span, int index) {
          DeletionSpan deletion = (DeletionSpan)span;

          switch (index) {
            case 0:
              return deletion.getReviewerName();

            case 1: {
              Date time = deletion.getReviewTime();
              if (time == null) break;
              return Long.toString(time.getTime());
            }

            case 2: {
              InsertionSpan insertion = deletion.getInsertion();
              if (insertion == null) break;
              return insertion.getReviewerName();
            }

            case 3: {
              InsertionSpan insertion = deletion.getInsertion();
              if (insertion == null) break;
              Date time = insertion.getReviewTime();
              if (time == null) break;
              return Long.toString(time.getTime());
            }
          }

          return null;
        }

        @Override
        public final Object newSpan (String[] properties) {
          String name = properties[0];
          Date time = newTime(properties[1]);
          String iname = properties[2];
          Date itime = newTime(properties[3]);

          Constructor constructor = LanguageUtilities.getConstructor(getType(), InsertionSpan.class);
          if (constructor == null) return null;
          InsertionSpan insertion;

          if (itime == null) {
            insertion = null;
          } else {
            insertion = new InsertionSpan();
            if (iname != null) insertion.setReviewerName(iname);
            if (itime != null) insertion.setReviewTime(itime);
          }

          DeletionSpan deletion = (DeletionSpan)LanguageUtilities.newInstance(constructor, insertion);
          if (deletion == null) return null;

          if (name != null) deletion.setReviewerName(name);
          if (time != null) deletion.setReviewTime(time);
          return deletion;
        }
      }
    );

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
              return comment.getReviewerName();

            case 3:
              return comment.getReviewerInitials();

            case 4:
              return Long.toString(comment.getReviewTime().getTime());
          }

          return null;
        }

        @Override
        public final Object newSpan (String[] properties) {
          Editable content = newContent(properties[0], properties[1]);
          if (content == null) return null;

          String name = properties[2];
          String initials = properties[3];
          Date time = newTime(properties[4]);

          Constructor constructor = LanguageUtilities.getConstructor(getType(), Editable.class);
          if (constructor == null) return null;

          CommentSpan comment = (CommentSpan)LanguageUtilities.newInstance(constructor, content);
          if (comment == null) return null;

          if (name != null) comment.setReviewerName(name);
          if (initials != null) comment.setReviewerInitials(initials);
          if (time != null) comment.setReviewTime(time);
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

  public final static boolean restoreSpans (Spannable content, String[] fields) {
    boolean added = false;
    int length = content.length();
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

        content.setSpan(span, start, end, flags);
        added = true;

        if (span instanceof EditorSpan) {
          ((EditorSpan)span).onSpanRestored(content);
        }
      }
    }

    return added;
  }

  public final static boolean restoreSpans (Spannable content, String spans) {
    if (spans == null) return false;

    spans = spans.trim();
    if (spans.isEmpty()) return false;

    String[] fields = spans.split("\\s+");
    return restoreSpans(content, fields);
  }
}

package org.nbp.common;

import java.lang.reflect.Constructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public abstract class Spans {
  private final static Map<String, Entry> spanMap = new HashMap<String, Entry>();

  public static class Entry {
    private final String spanIdentifier;
    private final Class<? extends CharacterStyle> objectType;

    public final String getIdentifier () {
      return spanIdentifier;
    }

    private final static Class[] argumentTypes = new Class[] {};
    private final static Object[] argumentValues = new Object[] {};

    protected Class[] getArgumentTypes () {
      return argumentTypes;
    }

    protected Object[] getArgumentValues () {
      return argumentValues;
    }

    public final CharacterStyle newInstance () {
      Constructor constructor = LanguageUtilities.getConstructor(objectType, getArgumentTypes());
      if (constructor == null) return null;
      return (CharacterStyle)LanguageUtilities.newInstance(constructor, getArgumentValues());
    }

    public boolean isFor (CharacterStyle span) {
      return LanguageUtilities.canAssign(objectType, span.getClass());
    }

    protected Entry (String identifier, Class<? extends CharacterStyle> type) {
      spanIdentifier = identifier;
      objectType = type;

      synchronized (spanMap) {
        spanMap.put(identifier, this);
      }
    }
  }

  public static abstract class StyleEntry extends Entry {
    protected abstract int getStyle ();

    private final static Class[] argumentTypes = new Class[] {int.class};

    @Override
    protected Class[] getArgumentTypes () {
      return argumentTypes;
    }

    @Override
    protected Object[] getArgumentValues () {
      return new Integer[] {getStyle()};
    }

    @Override
    public boolean isFor (CharacterStyle span) {
      if (!super.isFor(span)) return false;
      return ((StyleSpan)span).getStyle() == getStyle();
    }

    protected StyleEntry (String identifier) {
      super(identifier, StyleSpan.class);
    }
  }

  public final static StyleEntry BOLD = new StyleEntry("B") {
    @Override
    protected final int getStyle () {
      return Typeface.BOLD;
    }
  };

  public final static StyleEntry BOLD_ITALIC = new StyleEntry("BI") {
    @Override
    protected final int getStyle () {
      return Typeface.BOLD_ITALIC;
    }
  };

  public final static StyleEntry ITALIC = new StyleEntry("I") {
    @Override
    protected final int getStyle () {
      return Typeface.ITALIC;
    }
  };

  public final static Entry UNDERLINE = new Entry("U", UnderlineSpan.class);

  private final static Entry[] spanArray = new Entry[] {
    BOLD,
    BOLD_ITALIC,
    ITALIC,
    UNDERLINE
  };

  public final static List<Entry> getEntries () {
    return Arrays.asList(spanArray);
  }

  public final static Entry getEntry (CharacterStyle span) {
    for (Entry entry : getEntries()) {
      if (entry.isFor(span)) return entry;
    }

    return null;
  }

  public final static Entry getEntry (String identifier) {
    synchronized (spanMap) {
      return spanMap.get(identifier);
    }
  }

  private Spans () {
  }
}

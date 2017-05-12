package org.nbp.editor;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Editable;

import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

public abstract class RegionSpan extends EditorSpan {
  private final String decorationPrefix;
  private final String decorationSuffix;
  private final CharacterStyle characterStyle;

  protected RegionSpan (String prefix, String suffix, CharacterStyle style) {
    super();
    decorationPrefix = prefix;
    decorationSuffix = suffix;
    characterStyle = style;
  }

  protected RegionSpan (String prefix, String suffix) {
    this(prefix, suffix, null);
  }

  protected RegionSpan (CharacterStyle style) {
    this(null, null, style);
  }

  public final String getDecorationPrefix () {
    return decorationPrefix;
  }

  public final String getDecorationSuffix () {
    return decorationSuffix;
  }

  public final CharacterStyle getCharacterStyle () {
    return characterStyle;
  }

  private CharSequence actualText;
  private CharSequence decoratedText;

  public final CharSequence getActualText () {
    return actualText;
  }

  public final CharSequence getDecoratedText () {
    return decoratedText;
  }

  private final void appendDecoration (Editable text, String decoration) {
    if (decoration != null) {
      if (!decoration.isEmpty()) {
        int start = text.length();
        text.append(decoration);
        int end = text.length();
        text.setSpan(new DecorationSpan(), start, end, text.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    }
  }

  protected Integer getColor () {
    return null;
  }

  public final void decorateText (Editable content) {
    int spanStart = content.getSpanStart(this);
    int spanEnd = content.getSpanEnd(this);
    actualText = content.subSequence(spanStart, spanEnd);

    Editable text = new SpannableStringBuilder();
    appendDecoration(text, decorationPrefix);
    int textStart = text.length();
    text.append(actualText);
    int textEnd = text.length();
    appendDecoration(text, decorationSuffix);

    if (characterStyle != null) {
      text.setSpan(characterStyle, textStart, textEnd, text.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    {
      Integer color = getColor();

      if (color != null) {
        text.setSpan(
          new ForegroundColorSpan(color),
          textStart, textEnd,
          text.SPAN_INCLUSIVE_EXCLUSIVE
        );
      }
    }

    decoratedText = text.subSequence(0, text.length());
    content.replace(spanStart, spanEnd, decoratedText);
  }

  public final void restoreData (Spannable text) {
    int start = text.getSpanStart(this);
    int end = text.getSpanEnd(this);
    decoratedText = text.subSequence(start, end);

    if (decorationPrefix != null) start += decorationPrefix.length();
    if (decorationSuffix != null) end -= decorationSuffix.length();
    actualText = text.subSequence(start, end);
  }
}

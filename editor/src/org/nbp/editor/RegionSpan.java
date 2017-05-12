package org.nbp.editor;

import android.text.Spannable;
import android.text.SpannableStringBuilder;

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

  private final void appendDecoration (SpannableStringBuilder sb, String decoration) {
    if (decoration != null) {
      if (!decoration.isEmpty()) {
        int start = sb.length();
        sb.append(decoration);
        int end = sb.length();
        sb.setSpan(new DecorationSpan(), start, end, sb.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    }
  }

  protected Integer getColor () {
    return null;
  }

  public final void decorateText (SpannableStringBuilder content) {
    int spanStart = content.getSpanStart(this);
    int spanEnd = content.getSpanEnd(this);
    actualText = content.subSequence(spanStart, spanEnd);

    SpannableStringBuilder sb = new SpannableStringBuilder();
    appendDecoration(sb, decorationPrefix);
    int textStart = sb.length();
    sb.append(actualText);
    int textEnd = sb.length();
    appendDecoration(sb, decorationSuffix);

    if (characterStyle != null) {
      sb.setSpan(characterStyle, textStart, textEnd, sb.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    {
      Integer color = getColor();

      if (color != null) {
        sb.setSpan(
          new ForegroundColorSpan(color),
          textStart, textEnd,
          content.SPAN_INCLUSIVE_EXCLUSIVE
        );
      }
    }

    decoratedText = sb.subSequence(0, sb.length());
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

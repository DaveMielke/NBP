package org.nbp.editor;

import android.text.Editable;
import android.text.Spanned;
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

  private final boolean appendDecoration (Editable text, String decoration) {
    if (decoration != null) {
      if (!decoration.isEmpty()) {
        int start = text.length();
        text.append(decoration);
        int end = text.length();

        text.setSpan(new DecorationSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return true;
      }
    }

    return false;
  }

  protected Integer getForegroundColor () {
    return null;
  }

  private final static int[] textFlagsArray = new int[] {
    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
    Spanned.SPAN_EXCLUSIVE_INCLUSIVE,
    Spanned.SPAN_INCLUSIVE_EXCLUSIVE,
    Spanned.SPAN_INCLUSIVE_INCLUSIVE
  };

  public final void decorateText (Editable content) {
    int spanStart = content.getSpanStart(this);
    int spanEnd = content.getSpanEnd(this);
    actualText = content.subSequence(spanStart, spanEnd);

    Editable text = new SpannableStringBuilder();
    int textFlagsIndex = 0;

    if (appendDecoration(text, decorationPrefix)) textFlagsIndex |= 0X2;
    int textStart = text.length();
    text.append(actualText);
    int textEnd = text.length();
    if (appendDecoration(text, decorationSuffix)) textFlagsIndex |= 0X1;
    int textFlags = textFlagsArray[textFlagsIndex];

    if (characterStyle != null) {
      text.setSpan(characterStyle, textStart, textEnd, textFlags);
    }

    {
      Integer color = getForegroundColor();

      if (color != null) {
        text.setSpan(
          new ForegroundColorSpan(color),
          textStart, textEnd, textFlags
        );
      }
    }

    decoratedText = text.subSequence(0, text.length());
    content.replace(spanStart, spanEnd, decoratedText);
  }

  @Override
  public void restore (Spannable text) {
    super.restore(text);

    int start = text.getSpanStart(this);
    int end = text.getSpanEnd(this);
    decoratedText = text.subSequence(start, end);

    if (decorationPrefix != null) start += decorationPrefix.length();
    if (decorationSuffix != null) end -= decorationSuffix.length();
    actualText = text.subSequence(start, end);
  }
}

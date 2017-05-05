package org.nbp.editor;

import java.util.Date;

import android.text.SpannableStringBuilder;
import android.text.Spannable;
import android.text.style.CharacterStyle;

public abstract class RevisionSpan extends EditorSpan {
  private final CharSequence actualText;
  private final String decorationPrefix;
  private final String decorationSuffix;
  private final CharacterStyle revisionStyle;

  private final CharSequence decoratedText;

  protected RevisionSpan (CharSequence text, String prefix, String suffix, CharacterStyle style) {
    super();
    actualText = text;
    decorationPrefix = prefix;
    decorationSuffix = suffix;
    revisionStyle = style;

    SpannableStringBuilder sb = new SpannableStringBuilder();
    sb.append(decorationPrefix);
    int start = sb.length();
    sb.append(actualText);
    int end = sb.length();
    sb.append(decorationSuffix);
    sb.setSpan(revisionStyle, start, end, sb.SPAN_INCLUSIVE_EXCLUSIVE);
    decoratedText = sb.subSequence(0, sb.length());
  }

  public final CharSequence getActualText () {
    return actualText;
  }

  public final CharSequence getDecoratedText () {
    return decoratedText;
  }

  public final CharacterStyle getStyle () {
    return revisionStyle;
  }

  public final void decorateText (SpannableStringBuilder content) {
    content.replace(
      content.getSpanStart(this),
      content.getSpanEnd(this),
      decoratedText
    );
  }

  private String revisionAuthor = null;
  private Date revisionTimestamp = null;

  public final String getAuthor () {
    return revisionAuthor;
  }

  public final RevisionSpan setAuthor (String author) {
    revisionAuthor = author;
    return this;
  }

  public final Date getTimestamp () {
    return revisionTimestamp;
  }

  public final RevisionSpan setTimestamp (Date timestamp) {
    revisionTimestamp = timestamp;
    return this;
  }

  public final RevisionSpan setTimestamp () {
    return setTimestamp(new Date());
  }
}

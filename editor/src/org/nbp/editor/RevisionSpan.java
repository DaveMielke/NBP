package org.nbp.editor;

import java.util.Date;

import android.text.Spannable;
import android.text.style.CharacterStyle;

public abstract class RevisionSpan extends EditorSpan {
  private final CharSequence revisionText;
  private final CharacterStyle revisionStyle;

  protected RevisionSpan (CharSequence text, CharacterStyle style) {
    super();
    revisionText = text;
    revisionStyle = style;
  }

  public final CharSequence getText () {
    return revisionText;
  }

  public final CharacterStyle getStyle () {
    return revisionStyle;
  }

  public final void addStyle (Spannable spannable) {
    spannable.setSpan(
      getStyle(),
      spannable.getSpanStart(this),
      spannable.getSpanEnd(this),
      spannable.getSpanFlags(this)
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

package org.nbp.editor;

import android.text.Spannable;
import android.text.style.CharacterStyle;

public abstract class RevisionSpan extends EditorSpan {
  private final CharSequence revisionText;
  private final CharacterStyle revisionStyle;

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

  protected RevisionSpan (CharSequence text, CharacterStyle style) {
    super();
    revisionText = text;
    revisionStyle = style;
  }
}

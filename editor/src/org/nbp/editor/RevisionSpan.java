package org.nbp.editor;

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

  protected RevisionSpan (CharSequence text, CharacterStyle style) {
    super();
    revisionText = text;
    revisionStyle = style;
  }
}

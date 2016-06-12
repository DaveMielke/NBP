package org.nbp.editor;

public abstract class RevisionSpan extends EditorSpan {
  private final CharSequence revisionText;

  public final CharSequence getText () {
    return revisionText;
  }

  protected RevisionSpan (CharSequence text) {
    super();
    revisionText = text;
  }
}

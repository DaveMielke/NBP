package org.nbp.editor;

public abstract class EditorSpan {
  public abstract int getSpanName ();
  public abstract String getSpanIdentifier ();
  public abstract boolean isHighlightSpan ();

  protected EditorSpan () {
  }
}

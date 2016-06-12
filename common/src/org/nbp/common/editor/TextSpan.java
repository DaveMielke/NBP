package org.nbp.common.editor;

public abstract class TextSpan extends EditorSpan {
  @Override
  public final boolean isHighlightSpan () {
    return true;
  }

  protected TextSpan () {
    super();
  }
}

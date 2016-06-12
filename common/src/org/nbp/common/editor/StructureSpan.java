package org.nbp.common.editor;

public abstract class StructureSpan extends EditorSpan {
  @Override
  public final boolean isHighlightSpan () {
    return false;
  }

  protected StructureSpan () {
    super();
  }
}

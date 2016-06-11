package org.nbp.editor;

public class ParagraphSpan extends StructureSpan {
  @Override
  public final int getSpanName () {
    return R.string.span_paragraph;
  }

  public ParagraphSpan () {
    super();
  }
}

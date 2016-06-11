package org.nbp.editor;

public class ParagraphSpan extends StructureSpan {
  @Override
  public final int getSpanName () {
    return R.string.span_paragraph;
  }

  @Override
  public final String getSpanIdentifier () {
    return "par";
  }

  public ParagraphSpan () {
    super();
  }
}

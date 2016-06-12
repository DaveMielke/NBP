package org.nbp.editor;

public class ParagraphSpan extends StructureSpan {
  @Override
  public final String getSpanIdentifier () {
    return "par";
  }

  public ParagraphSpan () {
    super();
  }
}

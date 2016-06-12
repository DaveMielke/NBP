package org.nbp.editor;

public class CommentSpan extends StructureSpan {
  @Override
  public final String getSpanIdentifier () {
    return "com";
  }

  public CommentSpan () {
    super();
  }
}

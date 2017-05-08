package org.nbp.editor;

public class DecorationSpan extends EditorSpan {
  public DecorationSpan () {
    super();
  }

  @Override
  public final String getSpanIdentifier () {
    return "dec";
  }
}

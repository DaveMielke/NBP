package org.nbp.editor;

public class InsertSpan extends RevisionSpan {
  @Override
  public final String getSpanIdentifier () {
    return "ins";
  }

  public InsertSpan (CharSequence text) {
    super(text);
  }
}

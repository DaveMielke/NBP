package org.nbp.editor;

public class DeleteSpan extends RevisionSpan {
  @Override
  public final String getSpanIdentifier () {
    return "del";
  }

  public DeleteSpan (CharSequence text) {
    super(text);
  }
}

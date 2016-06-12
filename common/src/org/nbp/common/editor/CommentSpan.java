package org.nbp.common.editor;
import org.nbp.common.R;

public class CommentSpan extends StructureSpan {
  @Override
  public final int getSpanName () {
    return R.string.editor_span_name_comment;
  }

  @Override
  public final String getSpanIdentifier () {
    return "com";
  }

  public CommentSpan () {
    super();
  }
}

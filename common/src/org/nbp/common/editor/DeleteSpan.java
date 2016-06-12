package org.nbp.common.editor;
import org.nbp.common.R;

public class DeleteSpan extends RevisionSpan {
  @Override
  public final int getSpanName () {
    return R.string.editor_span_name_delete;
  }

  @Override
  public final String getSpanIdentifier () {
    return "del";
  }

  public DeleteSpan () {
    super();
  }
}

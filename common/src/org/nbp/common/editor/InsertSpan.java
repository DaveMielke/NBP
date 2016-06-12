package org.nbp.common.editor;
import org.nbp.common.R;

public class InsertSpan extends RevisionSpan {
  @Override
  public final int getSpanName () {
    return R.string.editor_span_name_insert;
  }

  @Override
  public final String getSpanIdentifier () {
    return "ins";
  }

  public InsertSpan () {
    super();
  }
}

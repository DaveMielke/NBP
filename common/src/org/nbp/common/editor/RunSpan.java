package org.nbp.common.editor;
import org.nbp.common.R;

public class RunSpan extends StructureSpan {
  @Override
  public final int getSpanName () {
    return R.string.editor_span_name_run;
  }

  @Override
  public final String getSpanIdentifier () {
    return "run";
  }

  public RunSpan () {
    super();
  }
}

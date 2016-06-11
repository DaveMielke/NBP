package org.nbp.editor;

public class RunSpan extends StructureSpan {
  @Override
  public final int getSpanName () {
    return R.string.span_run;
  }

  @Override
  public final String getSpanIdentifier () {
    return "run";
  }

  public RunSpan () {
    super();
  }
}

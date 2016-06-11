package org.nbp.editor;

public class SectionSpan extends StructureSpan {
  @Override
  public final int getSpanName () {
    return R.string.span_section;
  }

  @Override
  public final String getSpanIdentifier () {
    return "sec";
  }

  public SectionSpan () {
    super();
  }
}

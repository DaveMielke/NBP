package org.nbp.common.editor;
import org.nbp.common.R;

public class SectionSpan extends StructureSpan {
  @Override
  public final int getSpanName () {
    return R.string.editor_span_name_section;
  }

  @Override
  public final String getSpanIdentifier () {
    return "sec";
  }

  public SectionSpan () {
    super();
  }
}

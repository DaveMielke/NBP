package org.nbp.editor;

public class InsertSpan extends RevisionSpan {
  private final static String prefixDecoration = "⣏⢎";
  private final static String suffixDecoration = "⡱⣹";

  public InsertSpan () {
    super(prefixDecoration, suffixDecoration);
  }

  @Override
  public final int getRevisionType () {
    return R.string.revision_type_insert;
  }

  @Override
  public CharSequence getRejectText () {
    return "";
  }
}

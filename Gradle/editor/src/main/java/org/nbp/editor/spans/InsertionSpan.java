package org.nbp.editor.spans;
import org.nbp.editor.*;

public class InsertionSpan extends RevisionSpan {
  private final static String prefixDecoration = "⣏⢎";
  private final static String suffixDecoration = "⡱⣹";

  public InsertionSpan () {
    super(prefixDecoration, suffixDecoration);
  }

  @Override
  public final int getRevisionType () {
    return R.string.revision_type_insert;
  }

  @Override
  public final CharSequence getAcceptText () {
    return getActualText();
  }

  @Override
  public final CharSequence getRejectText () {
    return "";
  }

  @Override
  public final CharSequence getRevertText () {
    return getRejectText();
  }
}

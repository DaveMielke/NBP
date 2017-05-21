package org.nbp.editor;

import android.text.style.StrikethroughSpan;

public class DeleteSpan extends RevisionSpan {
  private final static String prefixDecoration = "⣏⡱";
  private final static String suffixDecoration = "⢎⣹";

  public DeleteSpan () {
    super(prefixDecoration, suffixDecoration);
  }

  @Override
  public final int getRevisionType () {
    return R.string.revision_type_delete;
  }

  @Override
  public CharSequence getAcceptText () {
    return "";
  }
}

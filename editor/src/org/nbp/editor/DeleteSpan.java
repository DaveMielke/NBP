package org.nbp.editor;

import android.text.style.StrikethroughSpan;

public class DeleteSpan extends RevisionSpan {
  private final static String decorationPrefix = "⣏⡱";
  private final static String decorationSuffix = "⢎⣹";

  public DeleteSpan () {
    super(decorationPrefix, decorationSuffix);
  }

  @Override
  public final int getRevisionType () {
    return R.string.revision_type_delete;
  }
}

package org.nbp.editor;

import android.text.style.StrikethroughSpan;

public class DeletionSpan extends RevisionSpan {
  private final static String prefixDecoration = "⣏⡱";
  private final static String suffixDecoration = "⢎⣹";

  public DeletionSpan () {
    super(prefixDecoration, suffixDecoration);
  }

  private boolean wasInsertion = false;

  public final boolean getWasInsertion () {
    return wasInsertion;
  }

  public final void setWasInsertion (boolean yes) {
    wasInsertion = yes;
  }

  @Override
  public final int getRevisionType () {
    return R.string.revision_type_delete;
  }

  @Override
  public final CharSequence getAcceptText () {
    return "";
  }

  @Override
  public final CharSequence getRejectText () {
    return getActualText();
  }

  @Override
  public final CharSequence getOriginalText () {
    return wasInsertion? getAcceptText(): getRejectText();
  }
}

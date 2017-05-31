package org.nbp.editor;

import android.text.style.StrikethroughSpan;

public class DeletionSpan extends RevisionSpan {
  private final static String prefixDecoration = "⣏⡱";
  private final static String suffixDecoration = "⢎⣹";

  private final InsertionSpan insertionRevision;

  public DeletionSpan (InsertionSpan insertion) {
    super(prefixDecoration, suffixDecoration);
    insertionRevision = insertion;
  }

  public DeletionSpan () {
    this(null);
  }

  public final InsertionSpan getInsertion () {
    return insertionRevision;
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
  public final CharSequence getRevertText () {
    return (insertionRevision != null)? getAcceptText(): getRejectText();
  }
}

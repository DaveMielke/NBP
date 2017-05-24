package org.nbp.editor;

import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

import android.text.style.CharacterStyle;
import java.util.Date;

public abstract class ReviewSpan extends RegionSpan {
  protected ReviewSpan (String prefix, String suffix, CharacterStyle style) {
    super(prefix, suffix, style);
  }

  protected ReviewSpan (String prefix, String suffix) {
    super(prefix, suffix);
  }

  protected ReviewSpan (CharacterStyle style) {
    super(style);
  }

  private String reviewerName = null;
  private String reviewerInitials = null;
  private Date reviewTimestamp = null;

  public final String getReviewerName () {
    return reviewerName;
  }

  public final void setReviewerName (String name) {
    reviewerName = name;
  }

  public final String getReviewerInitials () {
    return reviewerInitials;
  }

  public final void setReviewerInitials (String initials) {
    reviewerInitials = initials;
  }

  public final Date getReviewTimestamp () {
    return reviewTimestamp;
  }

  public final void setReviewTimestamp (Date timestamp) {
    reviewTimestamp = timestamp;
  }

  public final void setReviewTimestamp () {
    setReviewTimestamp(new Date());
  }

  @Override
  protected final Integer getForegroundColor () {
    return ReviewerColors.get(getReviewerName());
  }

  @Override
  public void finishDialog (DialogHelper helper) {
    super.finishDialog(helper);
    helper.setText(R.id.review_name, getReviewerName());
    helper.setText(R.id.review_initials, getReviewerInitials());

    {
      Date timestamp = getReviewTimestamp();
      if (timestamp != null) helper.setText(R.id.review_timestamp, timestamp.toString());
    }
  }
}

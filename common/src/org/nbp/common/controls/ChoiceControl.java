package org.nbp.common.controls;
import org.nbp.common.*;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;

public abstract class ChoiceControl extends IntegerControl {
  protected abstract int getValueCount ();
  protected abstract String getValueLabel (int index);
  public abstract CharSequence[] getHighlightedLabels ();

  private String[] plainLabels = null;

  protected final String[] getPlainLabels () {
    synchronized (this) {
      if (plainLabels == null) plainLabels = new String[getValueCount()];
      return plainLabels;
    }
  }

  public final String getPlainLabel (int index) {
    String[] labels = getPlainLabels();

    synchronized (labels) {
      String label = labels[index];
      if (label != null) return label;
      return labels[index] = getValueLabel(index);
    }
  }

  private String getLabelForChange (int index, int resource) {
    int count = getValueCount();
    if (count < 2) return null;
    if (count == 2) return getValueLabel(index);
    return getString(resource);
  }

  @Override
  protected int getResourceForNext () {
    return R.string.control_next_default;
  }

  @Override
  public String getLabelForNext () {
    return getLabelForChange(1, getResourceForNext());
  }

  @Override
  protected int getResourceForPrevious () {
    return R.string.control_previous_default;
  }

  @Override
  public String getLabelForPrevious () {
    return getLabelForChange(0, getResourceForPrevious());
  }

  protected final CharSequence highlightUnselectableLabel (CharSequence label) {
    Spannable text = new SpannableString(label);
    text.setSpan(new StrikethroughSpan(), 0, text.length(), text.SPAN_EXCLUSIVE_EXCLUSIVE);
    return text;
  }

  private final static Integer minimumValue = 0;
  private static Integer maximumValue = null;

  @Override
  protected Integer getIntegerMinimum () {
    return minimumValue;
  }

  @Override
  protected Integer getIntegerMaximum () {
    synchronized (minimumValue) {
      if (maximumValue == null) maximumValue = getValueCount() - 1;
    }

    return maximumValue;
  }

  protected ChoiceControl () {
    super();
  }
}

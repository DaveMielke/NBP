package org.nbp.editor.controls;
import org.nbp.editor.*;

import org.nbp.common.controls.IntegerControl;

public class SizeLimitControl extends IntegerControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SizeLimit;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "size-limit";
  }

  @Override
  protected int getIntegerScale () {
    return 25000;
  }

  private final Integer MINIMUM_VALUE = 1 * getIntegerScale();
  private final Integer MAXIMUM_VALUE = 20 * getIntegerScale();

  @Override
  protected Integer getIntegerMinimum () {
    return MINIMUM_VALUE;
  }

  @Override
  protected Integer getIntegerMaximum () {
    return MAXIMUM_VALUE;
  }

  @Override
  protected int getIntegerDefault () {
    return ApplicationDefaults.SIZE_LIMIT;
  }

  @Override
  public int getIntegerValue () {
    return ApplicationSettings.SIZE_LIMIT;
  }

  @Override
  protected boolean setIntegerValue (int value) {
    if (value < 1) return false;
    ApplicationSettings.SIZE_LIMIT = value;
    return true;
  }

  public SizeLimitControl () {
    super();
  }
}

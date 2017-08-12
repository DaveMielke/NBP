package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.DurationControl;

public class SpaceTimeoutControl extends DurationControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpaceTimeout;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_one_hand;
  }

  @Override
  protected String getPreferenceKey () {
    return "space-timeout";
  }

  @Override
  protected final int getIntegerScale () {
    return MILLISECONDS_PER_SECOND / 2;
  }

  private final static Integer MAXIMUM_VALUE = 5 * MILLISECONDS_PER_SECOND;

  @Override
  protected Integer getIntegerMaximum () {
    return MAXIMUM_VALUE;
  }

  @Override
  protected int getIntegerDefault () {
    return ApplicationDefaults.SPACE_TIMEOUT;
  }

  @Override
  public int getIntegerValue () {
    return ApplicationSettings.SPACE_TIMEOUT;
  }

  @Override
  protected boolean setIntegerValue (int value) {
    ApplicationSettings.SPACE_TIMEOUT = value;
    return true;
  }

  public SpaceTimeoutControl () {
    super();
  }
}

package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.DurationControl;

public class PressedTimeoutControl extends DurationControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_PressedTimeout;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_one_hand;
  }

  @Override
  protected String getPreferenceKey () {
    return "pressed-timeout";
  }

  @Override
  protected final int getIntegerScale () {
    return 5 * MILLISECONDS_PER_SECOND;
  }

  private final static Integer MAXIMUM_VALUE = SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;

  @Override
  protected Integer getIntegerMaximum () {
    return MAXIMUM_VALUE;
  }

  @Override
  protected int getIntegerDefault () {
    return ApplicationDefaults.PRESSED_TIMEOUT;
  }

  @Override
  public int getIntegerValue () {
    return ApplicationSettings.PRESSED_TIMEOUT;
  }

  @Override
  protected boolean setIntegerValue (int value) {
    ApplicationSettings.PRESSED_TIMEOUT = value;
    return true;
  }

  public PressedTimeoutControl () {
    super();
  }
}

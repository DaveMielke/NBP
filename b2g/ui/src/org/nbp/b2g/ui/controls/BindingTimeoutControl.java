package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.TimeControl;

public class BindingTimeoutControl extends TimeControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_BindingTimeout;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_one_hand;
  }

  @Override
  protected String getPreferenceKey () {
    return "binding-timeout";
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
    return ApplicationDefaults.BINDING_TIMEOUT;
  }

  @Override
  public int getIntegerValue () {
    return ApplicationSettings.BINDING_TIMEOUT;
  }

  @Override
  protected boolean setIntegerValue (int value) {
    ApplicationSettings.BINDING_TIMEOUT = value;
    return true;
  }

  public BindingTimeoutControl () {
    super();
  }
}

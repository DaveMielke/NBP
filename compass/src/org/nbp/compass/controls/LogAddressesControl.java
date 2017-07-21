package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.BooleanControl;

public class LogAddressesControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogAddresses;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-addresses";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_ADDRESSES;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_ADDRESSES;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_ADDRESSES = value;
    return true;
  }

  public LogAddressesControl () {
    super();
  }
}

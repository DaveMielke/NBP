package org.nbp.ipaws.controls;
import org.nbp.ipaws.*;

import org.nbp.common.controls.StringControl;

public class PrimaryServerControl extends StringControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_PrimaryServer;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "primary-server";
  }

  @Override
  protected String getStringDefault () {
    return ApplicationDefaults.PRIMARY_SERVER;
  }

  @Override
  public String getStringValue () {
    return ApplicationSettings.PRIMARY_SERVER;
  }

  @Override
  protected boolean setStringValue (String value) {
    ApplicationSettings.PRIMARY_SERVER = value;
    return true;
  }

  public PrimaryServerControl () {
    super();
  }
}

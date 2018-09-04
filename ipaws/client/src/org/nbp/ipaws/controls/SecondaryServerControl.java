package org.nbp.ipaws.controls;
import org.nbp.ipaws.*;

import org.nbp.common.controls.StringControl;

public class SecondaryServerControl extends StringControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SecondaryServer;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "secondary-server";
  }

  @Override
  protected String getStringDefault () {
    return ApplicationDefaults.SECONDARY_SERVER;
  }

  @Override
  public String getStringValue () {
    return ApplicationSettings.SECONDARY_SERVER;
  }

  @Override
  protected boolean setStringValue (String value) {
    ApplicationSettings.SECONDARY_SERVER = value;
    return true;
  }

  public SecondaryServerControl () {
    super();
  }
}

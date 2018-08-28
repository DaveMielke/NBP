package org.nbp.ipaws.controls;
import org.nbp.ipaws.*;

import org.nbp.common.controls.StringControl;

public class ServerNameControl extends StringControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ServerName;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "server-name";
  }

  @Override
  protected String getStringDefault () {
    return ApplicationDefaults.SERVER_NAME;
  }

  @Override
  public String getStringValue () {
    return ApplicationSettings.SERVER_NAME;
  }

  @Override
  protected boolean setStringValue (String value) {
    ApplicationSettings.SERVER_NAME = value;
    return true;
  }

  public ServerNameControl () {
    super();
  }
}

package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.BooleanControl;

public class LogNavigationControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogNavigation;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-navigation";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_NAVIGATION;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_NAVIGATION;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_NAVIGATION = value;
    return true;
  }

  public LogNavigationControl () {
    super();
  }
}

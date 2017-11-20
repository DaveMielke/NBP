package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class LogEmulationsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogEmulations;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-emulations";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_EMULATIONS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_EMULATIONS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_EMULATIONS = value;
    return true;
  }

  public LogEmulationsControl () {
    super();
  }
}

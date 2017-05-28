package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.BooleanControl;

public class LogGesturesControl extends BooleanControl {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogGestures;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-gestures";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_GESTURES;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_GESTURES;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_GESTURES = value;
    return true;
  }

  public LogGesturesControl () {
    super();
  }
}

package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class CrashEmailsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_CrashEmails;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "crash-emails";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.CRASH_EMAILS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.CRASH_EMAILS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.CRASH_EMAILS = value;
    return true;
  }

  public CrashEmailsControl () {
    super();
  }
}

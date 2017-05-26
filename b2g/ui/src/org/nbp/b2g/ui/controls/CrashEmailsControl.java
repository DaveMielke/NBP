package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class CrashEmailsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_CrashEmails;
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
    super(ControlGroup.DEVELOPER);
  }
}

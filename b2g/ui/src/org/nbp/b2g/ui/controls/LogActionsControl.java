package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class LogActionsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogActions;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-actions";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_ACTIONS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_ACTIONS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_ACTIONS = value;
    return true;
  }

  public LogActionsControl () {
    super(ControlGroup.DEVELOPER);
  }
}

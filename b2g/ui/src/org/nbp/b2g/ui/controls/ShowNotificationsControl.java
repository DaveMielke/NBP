package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class ShowNotificationsControl extends BooleanControl {
  @Override
  public int getLabel () {
    return R.string.ShowNotifications_control_label;
  }

  @Override
  protected String getPreferenceKey () {
    return "show-notifications";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.SHOW_NOTIFICATIONS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.SHOW_NOTIFICATIONS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SHOW_NOTIFICATIONS = value;
    return true;
  }

  public ShowNotificationsControl () {
    super(ControlGroup.GENERAL);
  }
}

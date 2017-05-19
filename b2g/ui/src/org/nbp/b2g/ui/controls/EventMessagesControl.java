package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class EventMessagesControl extends BooleanControl {
  @Override
  public int getLabel () {
    return R.string.control_label_EventMessages;
  }

  @Override
  protected String getPreferenceKey () {
    return "event-messages";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.EVENT_MESSAGES;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.EVENT_MESSAGES;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.EVENT_MESSAGES = value;
    return true;
  }

  public EventMessagesControl () {
    super(ControlGroup.DEVELOPER);
  }
}

package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class SleepTalkControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SleepTalk;
  }

  @Override
  protected String getPreferenceKey () {
    return "sleep-talk";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.SLEEP_TALK;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.SLEEP_TALK;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SLEEP_TALK = value;
    return true;
  }

  public SleepTalkControl () {
    super(ControlGroup.SPEECH);
  }
}

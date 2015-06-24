package org.nbp.b2g.ui;

public class SleepTalkControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.sleepTalk_control_label);
  }

  @Override
  public String getNextLabel () {
    return ApplicationContext.getString(R.string.sleepTalk_control_next);
  }

  @Override
  public String getPreviousLabel () {
    return ApplicationContext.getString(R.string.sleepTalk_control_previous);
  }

  @Override
  protected String getPreferenceKey () {
    return "sleep-talk";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_SLEEP_TALK;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationSettings.SLEEP_TALK;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SLEEP_TALK = value;
    return true;
  }

  public SleepTalkControl () {
    super(false);
  }
}

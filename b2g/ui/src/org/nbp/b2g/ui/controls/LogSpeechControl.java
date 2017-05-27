package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class LogSpeechControl extends BooleanControl {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogSpeech;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-speech";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_SPEECH;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_SPEECH;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_SPEECH = value;
    return true;
  }

  public LogSpeechControl () {
    super();
  }
}

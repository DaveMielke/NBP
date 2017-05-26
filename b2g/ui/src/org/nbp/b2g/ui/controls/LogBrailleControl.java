package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class LogBrailleControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogBraille;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-braille";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_BRAILLE;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_BRAILLE;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_BRAILLE = value;
    return true;
  }

  public LogBrailleControl () {
    super(ControlGroup.DEVELOPER);
  }
}

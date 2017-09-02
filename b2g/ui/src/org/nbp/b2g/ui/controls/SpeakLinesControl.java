package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class SpeakLinesControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeakLines;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "speak-lines";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.SPEAK_LINES;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.SPEAK_LINES;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SPEAK_LINES = value;
    return true;
  }

  public SpeakLinesControl () {
    super();
  }
}

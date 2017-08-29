package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class SpeechEnabledControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechEnabled;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-enabled";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.SPEECH_ENABLED;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.SPEECH_ENABLED;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    if (!value) {
      if (!Controls.brailleEnabled.getBooleanValue()) {
        ApplicationUtilities.message(R.string.error_braille_off);
        return false;
      }
    }

    ApplicationSettings.SPEECH_ENABLED = value;
    return true;
  }

  public SpeechEnabledControl () {
    super();
  }
}

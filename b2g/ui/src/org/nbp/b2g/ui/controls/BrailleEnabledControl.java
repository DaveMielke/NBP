package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class BrailleEnabledControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_BrailleEnabled;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_braille;
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-enabled";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.BRAILLE_ENABLED;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.BRAILLE_ENABLED;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    if (!Controls.amRestoringControls()) {
      if (!value) {
        if (!Controls.speechEnabled.getBooleanValue()) {
          ApplicationUtilities.message(R.string.error_speech_off);
          return false;
        }
      }
    }

    BrailleDevice braille = Devices.braille.get();
    if (!braille.setEnabled(value)) return false;

    ApplicationSettings.BRAILLE_ENABLED = value;
    if (value) braille.refresh();
    return true;
  }

  public BrailleEnabledControl () {
    super();
  }
}

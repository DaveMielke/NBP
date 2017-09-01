package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class EchoCharactersControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_EchoCharacters;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "echo-characters";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.ECHO_CHARACTERS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.ECHO_CHARACTERS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.ECHO_CHARACTERS = value;
    return true;
  }

  public EchoCharactersControl () {
    super();
  }
}

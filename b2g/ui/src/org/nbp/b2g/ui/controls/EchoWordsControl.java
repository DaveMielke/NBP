package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class EchoWordsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_EchoWords;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "echo-words";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.ECHO_WORDS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.ECHO_WORDS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.ECHO_WORDS = value;
    return true;
  }

  public EchoWordsControl () {
    super();
  }
}

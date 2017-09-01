package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class EchoDeletionsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_EchoDeletions;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "echo-deletions";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.ECHO_DELETIONS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.ECHO_DELETIONS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.ECHO_DELETIONS = value;
    return true;
  }

  public EchoDeletionsControl () {
    super();
  }
}

package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class EchoSelectionControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_EchoSelection;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "echo-selection";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.ECHO_SELECTION;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.ECHO_SELECTION;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.ECHO_SELECTION = value;
    return true;
  }

  public EchoSelectionControl () {
    super();
  }
}

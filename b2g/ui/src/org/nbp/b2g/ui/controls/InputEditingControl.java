package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class InputEditingControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_InputEditing;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_keyboard;
  }

  @Override
  protected String getPreferenceKey () {
    return "input-editing";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.INPUT_EDITING;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.INPUT_EDITING;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.INPUT_EDITING = value;
    return true;
  }

  public InputEditingControl () {
    super();
  }
}

package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class EditingEnabledControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_EditingEnabled;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_keyboard;
  }

  @Override
  protected String getPreferenceKey () {
    return "editing-enabled";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.EDITING_ENABLED;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.EDITING_ENABLED;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.EDITING_ENABLED = value;
    return true;
  }

  public EditingEnabledControl () {
    super();
  }
}

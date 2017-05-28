package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.BooleanControl;

public class AdvancedActionsControl extends BooleanControl {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_AdvancedActions;
  }

  @Override
  protected String getPreferenceKey () {
    return "advanced-actions";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.ADVANCED_ACTIONS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.ADVANCED_ACTIONS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.ADVANCED_ACTIONS = value;
    return true;
  }

  public AdvancedActionsControl () {
    super();
  }
}

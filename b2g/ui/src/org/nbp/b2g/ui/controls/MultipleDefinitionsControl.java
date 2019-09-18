package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class MultipleDefinitionsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_MultipleDefinitions;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_dictionary;
  }

  @Override
  protected String getPreferenceKey () {
    return "multiple-definitions";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.MULTIPLE_DEFINITIONS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.MULTIPLE_DEFINITIONS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.MULTIPLE_DEFINITIONS = value;
    return true;
  }

  public MultipleDefinitionsControl () {
    super();
  }
}

package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.BooleanControl;

public class ShowHighlightedControl extends BooleanControl {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_braille;
  }

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ShowHighlighted;
  }

  @Override
  protected String getPreferenceKey () {
    return "show-highlighted";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.SHOW_HIGHLIGHTED;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.SHOW_HIGHLIGHTED;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SHOW_HIGHLIGHTED = value;
    return true;
  }

  public ShowHighlightedControl () {
    super();
  }
}

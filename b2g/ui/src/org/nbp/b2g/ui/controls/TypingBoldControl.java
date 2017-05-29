package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.BooleanControl;

public class TypingBoldControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_TypingBold;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_input;
  }

  @Override
  protected String getPreferenceKey () {
    return "typing-bold";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.TYPING_BOLD;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.TYPING_BOLD;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.TYPING_BOLD = value;
    return true;
  }

  public TypingBoldControl () {
    super();
  }
}

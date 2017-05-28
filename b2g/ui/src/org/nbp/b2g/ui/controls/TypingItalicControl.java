package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.BooleanControl;

public class TypingItalicControl extends BooleanControl {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_input;
  }

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_TypingItalic;
  }

  @Override
  protected String getPreferenceKey () {
    return "typing-italic";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.TYPING_ITALIC;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.TYPING_ITALIC;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.TYPING_ITALIC = value;
    return true;
  }

  public TypingItalicControl () {
    super();
  }
}

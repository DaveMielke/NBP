package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class TypingUnderlineControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.TypingUnderline_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "typing-underline";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.TYPING_UNDERLINE;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.TYPING_UNDERLINE;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.TYPING_UNDERLINE = value;
    return true;
  }

  public TypingUnderlineControl () {
    super(ControlGroup.TYPING);
  }
}

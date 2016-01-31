package org.nbp.b2g.ui;

public class TypingBoldControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.TypingBold_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "typing-bold";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_TYPING_BOLD;
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
    super(false);
  }
}

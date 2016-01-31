package org.nbp.b2g.ui;

public class InputBoldControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.InputBold_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "input-bold";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_INPUT_BOLD;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.INPUT_BOLD;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.INPUT_BOLD = value;
    return true;
  }

  public InputBoldControl () {
    super(false);
  }
}

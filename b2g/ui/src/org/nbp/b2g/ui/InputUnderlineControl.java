package org.nbp.b2g.ui;

public class InputUnderlineControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.InputUnderline_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "input-underline";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_INPUT_UNDERLINE;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.INPUT_UNDERLINE;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.INPUT_UNDERLINE = value;
    return true;
  }

  public InputUnderlineControl () {
    super(false);
  }
}

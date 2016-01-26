package org.nbp.b2g.ui;

public class InputModeControl extends EnumerationControl<InputMode> {
  @Override
  public CharSequence getLabel () {
    return toHeader(R.string.InputMode_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "input-mode";
  }

  @Override
  protected InputMode getEnumerationDefault () {
    return ApplicationParameters.DEFAULT_INPUT_MODE;
  }

  @Override
  public InputMode getEnumerationValue () {
    return ApplicationSettings.INPUT_MODE;
  }

  @Override
  protected boolean setEnumerationValue (InputMode value) {
    ApplicationSettings.INPUT_MODE = value;
    return true;
  }

  public InputModeControl () {
    super(false);
  }
}

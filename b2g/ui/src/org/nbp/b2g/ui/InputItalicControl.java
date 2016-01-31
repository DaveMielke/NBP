package org.nbp.b2g.ui;

public class InputItalicControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.InputItalic_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "input-italic";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_INPUT_ITALIC;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.INPUT_ITALIC;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.INPUT_ITALIC = value;
    return true;
  }

  public InputItalicControl () {
    super(false);
  }
}

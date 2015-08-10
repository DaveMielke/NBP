package org.nbp.b2g.ui;

public class BrailleInputControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return ApplicationContext.getString(R.string.BrailleInput_control_label);
  }

  @Override
  public CharSequence getNextLabel () {
    return ApplicationContext.getString(R.string.BrailleInput_control_next);
  }

  @Override
  public CharSequence getPreviousLabel () {
    return ApplicationContext.getString(R.string.BrailleInput_control_previous);
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-input";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_BRAILLE_INPUT;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationSettings.BRAILLE_INPUT;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.BRAILLE_INPUT = value;
    return true;
  }

  public BrailleInputControl () {
    super(false);
  }
}

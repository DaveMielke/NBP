package org.nbp.b2g.ui;

public class TypingModeControl extends EnumerationControl<TypingMode> {
  @Override
  public CharSequence getLabel () {
    return toHeader(R.string.TypingMode_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "typing-mode";
  }

  @Override
  protected TypingMode getEnumerationDefault () {
    return ApplicationDefaults.TYPING_MODE;
  }

  @Override
  public TypingMode getEnumerationValue () {
    return ApplicationSettings.TYPING_MODE;
  }

  @Override
  protected boolean setEnumerationValue (TypingMode value) {
    ApplicationSettings.TYPING_MODE = value;
    return true;
  }

  public TypingModeControl () {
    super(false);
  }
}
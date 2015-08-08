package org.nbp.b2g.ui;

public class BrailleEnabledControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return toHeader(ApplicationContext.getString(R.string.BrailleEnabled_control_label));
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-enabled";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_BRAILLE_ENABLED;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationSettings.BRAILLE_ENABLED;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    BrailleDevice braille = Devices.braille.get();
    if (!(value? braille.enable(): braille.disable())) return false;

    ApplicationSettings.BRAILLE_ENABLED = value;
    return true;
  }

  public BrailleEnabledControl () {
    super(false);
  }
}

package org.nbp.b2g.ui;

public class BrailleFirmnessControl extends IntegerControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.BrailleFirmness_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-firmness";
  }

  @Override
  protected int getIntegerDefault () {
    return ApplicationParameters.DEFAULT_BRAILLE_FIRMNESS;
  }

  @Override
  public int getIntegerValue () {
    return ApplicationSettings.BRAILLE_FIRMNESS;
  }

  @Override
  protected boolean setIntegerValue (int value) {
    if (!Devices.braille.get().setFirmness(value)) return false;
    ApplicationSettings.BRAILLE_FIRMNESS = value;
    return true;
  }

  public BrailleFirmnessControl () {
    super(false);
  }
}

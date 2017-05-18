package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class BrailleFirmnessControl extends IntegerControl {
  @Override
  public int getLabel () {
    return R.string.control_label_BrailleFirmness;
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-firmness";
  }

  @Override
  protected int getIntegerDefault () {
    return ApplicationDefaults.BRAILLE_FIRMNESS;
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
    super(ControlGroup.BRAILLE);
  }
}

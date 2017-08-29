package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.EnumerationControl;

public class BrailleFirmnessControl extends EnumerationControl<GenericLevel> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_BrailleFirmness;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_braille;
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-firmness";
  }

  @Override
  protected GenericLevel getEnumerationDefault () {
    return ApplicationDefaults.BRAILLE_FIRMNESS;
  }

  @Override
  public GenericLevel getEnumerationValue () {
    return ApplicationSettings.BRAILLE_FIRMNESS;
  }

  @Override
  protected boolean setEnumerationValue (GenericLevel value) {
    if (!Devices.braille.get().setFirmness(value)) return false;
    ApplicationSettings.BRAILLE_FIRMNESS = value;
    return true;
  }

  public BrailleFirmnessControl () {
    super();
  }
}

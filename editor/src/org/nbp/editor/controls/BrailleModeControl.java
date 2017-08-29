package org.nbp.editor.controls;
import org.nbp.editor.*;

import org.nbp.common.controls.EnumerationControl;

public class BrailleModeControl extends EnumerationControl<BrailleMode> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_BrailleMode;
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-mode";
  }

  @Override
  protected BrailleMode getEnumerationDefault () {
    return ApplicationDefaults.BRAILLE_MODE;
  }

  @Override
  public BrailleMode getEnumerationValue () {
    return ApplicationSettings.BRAILLE_MODE;
  }

  @Override
  protected boolean setEnumerationValue (BrailleMode value) {
    ApplicationSettings.BRAILLE_MODE = value;
    return true;
  }

  public BrailleModeControl () {
    super();
  }
}

package org.nbp.editor.controls;
import org.nbp.editor.*;

import org.nbp.common.controls.EnumerationControl;

public class BrailleCodeControl extends EnumerationControl<BrailleCode> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_BrailleCode;
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-code";
  }

  @Override
  protected BrailleCode getEnumerationDefault () {
    return ApplicationDefaults.BRAILLE_CODE;
  }

  @Override
  public BrailleCode getEnumerationValue () {
    return ApplicationSettings.BRAILLE_CODE;
  }

  @Override
  protected boolean setEnumerationValue (BrailleCode value) {
    ApplicationSettings.BRAILLE_CODE = value;
    return true;
  }

  public BrailleCodeControl () {
    super();
  }
}

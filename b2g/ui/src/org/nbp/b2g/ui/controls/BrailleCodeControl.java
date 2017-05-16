package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class BrailleCodeControl extends EnumerationControl<BrailleCode> {
  @Override
  public int getLabel () {
    return R.string.BrailleCode_control_label;
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-code";
  }

  @Override
  protected String getValueLabel (BrailleCode code) {
    return code.getTranslationEnumeration().getDescription();
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
    TranslationUtilities.refresh();
    return true;
  }

  public BrailleCodeControl () {
    super(ControlGroup.RENDERING);
  }
}

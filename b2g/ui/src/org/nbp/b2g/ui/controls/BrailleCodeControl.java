package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class BrailleCodeControl extends EnumerationControl<BrailleCode> {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_BrailleCode;
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
    super();
  }
}

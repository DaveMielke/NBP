package org.nbp.b2g.ui;

public class BrailleCodeControl extends EnumerationControl<BrailleCode> {
  @Override
  public CharSequence getLabel () {
    return toHeader(ApplicationContext.getString(R.string.BrailleCode_control_label));
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-code";
  }

  @Override
  protected String getValueLabel (BrailleCode code) {
    return code.getTranslationTable().getDescription();
  }

  @Override
  protected BrailleCode getEnumerationDefault () {
    return ApplicationParameters.DEFAULT_BRAILLE_CODE;
  }

  @Override
  public BrailleCode getEnumerationValue () {
    return ApplicationSettings.BRAILLE_CODE;
  }

  @Override
  protected boolean setEnumerationValue (BrailleCode value) {
    ApplicationSettings.BRAILLE_CODE = value;

    if (ApplicationSettings.LITERARY_BRAILLE) {
      Endpoints.getCurrentEndpoint().refresh();
    }

    return true;
  }

  public BrailleCodeControl () {
    super(false);
  }
}

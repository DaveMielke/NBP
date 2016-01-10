package org.nbp.b2g.ui;

public class LiteraryBrailleControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return toHeader(ApplicationContext.getString(R.string.LiteraryBraille_control_label));
  }

  @Override
  protected String getPreferenceKey () {
    return "literary-braille";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_LITERARY_BRAILLE;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LITERARY_BRAILLE;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LITERARY_BRAILLE = value;

    Endpoint endpoint = Endpoints.getCurrentEndpoint();
    endpoint.refreshBrailleTranslation();
    endpoint.write();

    return true;
  }

  public LiteraryBrailleControl () {
    super(false);
  }
}

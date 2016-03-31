package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class LiteraryBrailleControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return toHeader(R.string.LiteraryBraille_control_label);
  }

  @Override
  public String getConfirmation () {
    return getString(
             getBooleanValue()?
               R.string.LiteraryBraille_control_on:
               R.string.LiteraryBraille_control_off
           );
  }

  @Override
  protected String getPreferenceKey () {
    return "literary-braille";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LITERARY_BRAILLE;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LITERARY_BRAILLE;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LITERARY_BRAILLE = value;
    Endpoints.getCurrentEndpoint().refresh();
    return true;
  }

  public LiteraryBrailleControl () {
    super(false);
  }
}

package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class LiteraryBrailleControl extends BooleanControl {
  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LiteraryBraille;
  }

  @Override
  public String getConfirmation () {
    return getString(
             getBooleanValue()?
               R.string.control_value_LiteraryBraille_on:
               R.string.control_value_LiteraryBraille_off
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
    super();
  }
}

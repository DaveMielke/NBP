package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class WordWrapControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.WordWrap_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "word-wrap";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.WORD_WRAP;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.WORD_WRAP;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.WORD_WRAP = value;
    Endpoints.getCurrentEndpoint().refresh();
    return true;
  }

  public WordWrapControl () {
    super(ControlGroup.GENERAL);
  }
}

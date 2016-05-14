package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class BrailleDisplayControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return toHeader(R.string.BrailleDisplay_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "braille-display";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.BRAILLE_DISPLAY;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.BRAILLE_DISPLAY;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.BRAILLE_DISPLAY = value;
    return true;
  }

  public BrailleDisplayControl () {
    super(false);
  }
}

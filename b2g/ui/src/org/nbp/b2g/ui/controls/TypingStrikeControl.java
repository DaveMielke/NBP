package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class TypingStrikeControl extends BooleanControl {
  @Override
  public int getLabel () {
    return R.string.control_label_TypingStrike;
  }

  @Override
  protected String getPreferenceKey () {
    return "typing-strike";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.TYPING_STRIKE;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.TYPING_STRIKE;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.TYPING_STRIKE = value;
    return true;
  }

  public TypingStrikeControl () {
    super(ControlGroup.INPUT);
  }
}

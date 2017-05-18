package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class TypingItalicControl extends BooleanControl {
  @Override
  public int getLabel () {
    return R.string.control_label_TypingItalic;
  }

  @Override
  protected String getPreferenceKey () {
    return "typing-italic";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.TYPING_ITALIC;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.TYPING_ITALIC;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.TYPING_ITALIC = value;
    return true;
  }

  public TypingItalicControl () {
    super(ControlGroup.INPUT);
  }
}

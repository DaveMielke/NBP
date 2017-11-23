package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.EnumerationControl;

public class CharacterCodeControl extends EnumerationControl<CharacterCode> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_CharacterCode;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "character-code";
  }

  @Override
  protected CharacterCode getEnumerationDefault () {
    return ApplicationDefaults.CHARACTER_CODE;
  }

  @Override
  public CharacterCode getEnumerationValue () {
    return ApplicationSettings.CHARACTER_CODE;
  }

  @Override
  protected boolean setEnumerationValue (CharacterCode value) {
    ApplicationSettings.CHARACTER_CODE = value;
    return true;
  }

  public CharacterCodeControl () {
    super();
  }
}

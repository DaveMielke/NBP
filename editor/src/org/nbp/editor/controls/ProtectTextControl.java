package org.nbp.editor.controls;
import org.nbp.editor.*;

import org.nbp.common.controls.BooleanControl;

public class ProtectTextControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ProtectText;
  }

  @Override
  protected String getPreferenceKey () {
    return "protect-text";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.PROTECT_TEXT;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.PROTECT_TEXT;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.PROTECT_TEXT = value;
    return true;
  }

  public ProtectTextControl () {
    super();
  }
}

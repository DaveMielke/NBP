package org.nbp.b2g.input;

import android.util.Log;

public class ControlModifierAction extends ToggleAction {
  private static final String LOG_TAG = ControlModifierAction.class.getName();

  private static ControlModifierAction controlModifier = null;

  public static ControlModifierAction getControlModifier () {
    return controlModifier;
  }

  public ControlModifierAction () {
    super("CONTROL_MODIFIER");
    controlModifier = this;
  }
}

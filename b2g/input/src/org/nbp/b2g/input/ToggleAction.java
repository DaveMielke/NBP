package org.nbp.b2g.input;

import android.util.Log;

public class ToggleAction extends Action {
  private static final String LOG_TAG = ToggleAction.class.getName();

  protected boolean toggleState = false;

  protected void setState (boolean state) {
    if (state != toggleState) {
      toggleState = state;

      if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
        Log.d(LOG_TAG, getName() + " " + (toggleState? "set": "unset"));
      }
    }
  }

  public boolean getState () {
    boolean state = toggleState;
    setState(false);
    return state;
  }

  @Override
  public final boolean performAction () {
    setState(!toggleState);
    return true;
  }

  public ToggleAction (String name) {
    super(name);
  }
}

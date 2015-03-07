package org.nbp.b2g.input;

import android.util.Log;

import android.view.ViewConfiguration;

public class PowerAction extends Action {
  private static final String LOG_TAG = PowerAction.class.getName();

  private final PowerKey powerKey = new PowerKey();

  @Override
  public boolean performAction () {
    if (powerKey.press()) {
      delay(ViewConfiguration.getGlobalActionKeyTimeout() + ApplicationParameters.LONG_PRESS_DELAY);

      if (powerKey.release()) {
        return true;
      }
    }

    return false;
  }

  public PowerAction () {
    super("power-dialog");
  }
}

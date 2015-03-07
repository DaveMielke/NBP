package org.nbp.b2g.input;

import android.util.Log;

public class PowerDialogAction extends Action {
  private static final String LOG_TAG = PowerDialogAction.class.getName();

  private final UInputDevice uinputDevice = new UInputDevice();

  @Override
  public boolean performAction () {
    if (uinputDevice.press()) {
      delay(1100);

      if (uinputDevice.release()) {
        return true;
      }
    }

    return false;
  }

  public PowerDialogAction () {
    super("power-dialog");
  }
}

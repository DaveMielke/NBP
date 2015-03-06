package org.nbp.b2g.input;

import android.util.Log;

import android.view.KeyEvent;

public class PowerDialogAction extends KeyAction {
  private static final String LOG_TAG = PowerDialogAction.class.getName();

  @Override
  public boolean performAction () {
    return sendKey(1000);
  }

  public PowerDialogAction () {
    super(KeyEvent.KEYCODE_POWER, "power-dialog");
  }
}

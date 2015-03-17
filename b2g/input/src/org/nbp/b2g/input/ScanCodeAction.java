package org.nbp.b2g.input;

import android.util.Log;

public class ScanCodeAction extends Action {
  private static final String LOG_TAG = ScanCodeAction.class.getName();

  private final int scanCode;
  private final long holdTime;

  protected static final KeyboardDevice keyboardDevice = new KeyboardDevice();

  @Override
  public boolean performAction () {
    if (scanCode != KeyboardDevice.NULL_SCAN_CODE) {
      if (keyboardDevice.pressKey(scanCode)) {
        if (holdTime > 0) ApplicationUtilities.sleep(holdTime + ApplicationParameters.LONG_PRESS_DELAY);

        if (keyboardDevice.releaseKey(scanCode)) {
          return true;
        }
      }
    }

    return false;
  }

  public ScanCodeAction (String name, long holdTime) {
    super("SCANCODE_" + name);
    this.scanCode = KeyboardDevice.getScanCode(name);
    this.holdTime = holdTime;
  }

  public static void add (int keyMask, String name, long holdTime) {
    add((keyMask | KeyMask.SCAN_CODE), new ScanCodeAction(name, holdTime));
  }

  public static void add (int keyMask, String name) {
    add(keyMask, name, 0);
  }
}

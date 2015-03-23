package org.nbp.b2g.input;

public class ScanCodeAction extends KeyCodeAction {
  public static final String NULL_SCAN_CODE = "NULL";

  private static final KeyboardDevice keyboardDevice = new KeyboardDevice();

  protected String getScanCode () {
    return NULL_SCAN_CODE;
  }

  private void logScanCodeEvent (String name, int value, boolean press) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      logKeyEvent("scan", name, value, press);
    }
  }

  @Override
  public boolean performAction () {
    String name = getScanCode();

    if (!name.equals(NULL_SCAN_CODE)) {
      int value = KeyboardDevice.getScanCode(name);

      if (value != KeyboardDevice.NULL_SCAN_CODE) {
        logScanCodeEvent(name, value, true);
        if (keyboardDevice.pressKey(value)) {
          waitForHoldTime();

          logScanCodeEvent(name, value, false);
          if (keyboardDevice.releaseKey(value)) {
            return true;
          }
        }
      }
    }

    return super.performAction();
  }

  protected ScanCodeAction () {
    super();
  }
}

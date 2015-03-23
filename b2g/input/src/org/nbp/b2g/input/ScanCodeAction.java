package org.nbp.b2g.input;

public class ScanCodeAction extends KeyCodeAction {
  public static final String NULL_SCAN_CODE = "NULL";

  private static final KeyboardDevice keyboardDevice = new KeyboardDevice();

  protected String getScanCode () {
    return NULL_SCAN_CODE;
  }

  @Override
  public boolean performAction () {
    String name = getScanCode();

    if (!name.equals(NULL_SCAN_CODE)) {
      int value = KeyboardDevice.getScanCode(name);

      if (value != KeyboardDevice.NULL_SCAN_CODE) {
        if (keyboardDevice.pressKey(value)) {
          waitForHoldTime();

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

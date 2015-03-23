package org.nbp.b2g.input;

public abstract class ScanCodeAction extends KeyCodeAction {
  public static final String NULL_SCAN_CODE = "NULL";

  private static final KeyboardDevice keyboardDevice = new KeyboardDevice();

  protected String getScanCode () {
    return NULL_SCAN_CODE;
  }

  protected static final int SCAN_CODE_SHIFT = KeyboardDevice.getScanCode("LEFTSHIFT");
  protected static final int SCAN_CODE_CONTROL = KeyboardDevice.getScanCode("LEFTCTRL");
  protected static final int SCAN_CODE_ALT = KeyboardDevice.getScanCode("LEFTALT");
  protected static final int SCAN_CODE_ALTGR = KeyboardDevice.getScanCode("RIGHTALT");
  protected static final int SCAN_CODE_GUI = KeyboardDevice.getScanCode("LEFTMETA");

  protected int[] getScanCodeModifiers () {
    return null;
  }

  private void log (int value, boolean press) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      logKeyEvent("scan", null, value, press);
    }
  }

  private boolean press (int key) {
    log(key, true);
    return keyboardDevice.pressKey(key);
  }

  private boolean press (int[] keys) {
    if (keys != null) {
      for (int key : keys) {
        if (!press(key)) return false;
      }
    }

    return true;
  }

  private boolean release (int key) {
    log(key, false);
    return keyboardDevice.releaseKey(key);
  }

  private boolean release (int[] keys) {
    if (keys != null) {
      for (int key : keys) {
        if (!release(key)) return false;
      }
    }

    return true;
  }

  @Override
  public boolean performAction () {
    String name = getScanCode();

    if (!name.equals(NULL_SCAN_CODE)) {
      int value = KeyboardDevice.getScanCode(name);

      if (value != KeyboardDevice.NULL_SCAN_CODE) {
        int[] modifiers = getScanCodeModifiers();

        if (press(modifiers)) {
          if (press(value)) {
            waitForHoldTime();

            if (release(value)) {
              if (release(modifiers)) {
                return true;
              }
            }
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

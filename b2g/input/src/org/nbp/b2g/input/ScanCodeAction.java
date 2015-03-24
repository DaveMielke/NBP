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

  @Override
  public boolean performAction () {
    String name = getScanCode();

    if (!name.equals(NULL_SCAN_CODE)) {
      int value = KeyboardDevice.getScanCode(name);

      if (value != KeyboardDevice.NULL_SCAN_CODE) {
        KeyCombinationSender keyCombinationSender = new KeyCombinationSender() {
          @Override
          protected boolean sendKeyPress (int key) {
            log(key, true);
            return keyboardDevice.pressKey(key);
          }

          @Override
          protected boolean sendKeyRelease (int key) {
            log(key, false);
            return keyboardDevice.releaseKey(key);
          }
        };

        if (keyCombinationSender.sendKeyCombination(value, getScanCodeModifiers())) {
          return true;
        }
      }
    }

    return super.performAction();
  }

  protected ScanCodeAction () {
    super();
  }
}

package org.nbp.b2g.ui;

public abstract class ScanCodeAction extends KeyCodeAction {
  public final static String NULL_SCAN_CODE = "NULL";

  protected String getScanCode () {
    return NULL_SCAN_CODE;
  }

  protected final static int SCAN_CODE_SHIFT = KeyboardDevice.getScanCode("LEFTSHIFT");
  protected final static int SCAN_CODE_CONTROL = KeyboardDevice.getScanCode("LEFTCTRL");
  protected final static int SCAN_CODE_ALT = KeyboardDevice.getScanCode("LEFTALT");
  protected final static int SCAN_CODE_ALTGR = KeyboardDevice.getScanCode("RIGHTALT");
  protected final static int SCAN_CODE_GUI = KeyboardDevice.getScanCode("LEFTMETA");

  protected int[] getScanCodeModifiers () {
    return null;
  }

  @Override
  public boolean performAction () {
    String name = getScanCode();

    if (!name.equals(NULL_SCAN_CODE)) {
      int value = KeyboardDevice.getScanCode(name);

      if (value != KeyboardDevice.NULL_SCAN_CODE) {
        KeyCombinationInjecter keyCombinationInjecter = new KeyCombinationInjecter() {
          @Override
          protected boolean injectKeyPress (int key) {
            return Keyboard.pressKey(key);
          }

          @Override
          protected boolean injectKeyRelease (int key) {
            return Keyboard.releaseKey(key);
          }

          @Override
          protected String getKeyType () {
            return "scan code";
          }
        };

        if (keyCombinationInjecter.injectKeyCombination(value, getScanCodeModifiers())) {
          return true;
        }
      }
    }

    return super.performAction();
  }

  protected ScanCodeAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}

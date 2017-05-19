package org.nbp.b2g.ui;

public abstract class ScanCodeAction extends KeyCodeAction {
  public final static String NULL_SCAN_CODE = "NULL";

  protected String getScanCode () {
    return NULL_SCAN_CODE;
  }

  protected final static int SCAN_CODE_SHIFT = Keyboard.getScanCodeValue("LEFTSHIFT");
  protected final static int SCAN_CODE_CONTROL = Keyboard.getScanCodeValue("LEFTCTRL");
  protected final static int SCAN_CODE_ALT = Keyboard.getScanCodeValue("LEFTALT");
  protected final static int SCAN_CODE_ALTGR = Keyboard.getScanCodeValue("RIGHTALT");
  protected final static int SCAN_CODE_GUI = Keyboard.getScanCodeValue("LEFTMETA");

  protected int[] getScanCodeModifiers () {
    return null;
  }

  @Override
  public boolean performAction () {
    String name = getScanCode();

    if (!name.equals(NULL_SCAN_CODE)) {
      int value = Keyboard.getScanCodeValue(name);

      if (value != Keyboard.NULL_SCAN_CODE) {
        KeyCombinationInjector keyCombinationInjector = new KeyCombinationInjector() {
          @Override
          protected boolean injectKeyPress (int key) {
            return Keyboard.pressKey(key);
          }

          @Override
          protected boolean injectKeyRelease (int key) {
            return Keyboard.releaseKey(key);
          }
        };

        if (keyCombinationInjector.injectKeyCombination(value, getScanCodeModifiers())) {
          return true;
        }
      }
    }

    return super.performAction();
  }

  protected ScanCodeAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}

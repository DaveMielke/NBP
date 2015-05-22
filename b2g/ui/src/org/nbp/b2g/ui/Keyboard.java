package org.nbp.b2g.ui;

public abstract class Keyboard {
  private final static KeyboardInjecter injecter = Devices.getKeyboardDevice();

  public static boolean pressKey (int key) {
    return injecter.keyboardPress(key);
  }

  public static boolean releaseKey (int key) {
    return injecter.keyboardRelease(key);
  }

  public static boolean injectKey (int key, boolean press) {
    return press? pressKey(key): releaseKey(key);
  }

  public static boolean injectKey (int key, long duration) {
    if (pressKey(key)) {
      ApplicationUtilities.sleep(duration);

      if (releaseKey(key)) {
        return true;
      }
    }

    return false;
  }

  public static boolean injectKey (int key) {
    return injectKey(key, 0);
  }

  private Keyboard () {
  }
}

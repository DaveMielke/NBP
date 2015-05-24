package org.nbp.b2g.ui;

public abstract class Keyboard {
  private final static KeyboardInjector injector = Devices.keyboard.get();

  public static boolean pressKey (int key) {
    return injector.keyboardPress(key);
  }

  public static boolean releaseKey (int key) {
    return injector.keyboardRelease(key);
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

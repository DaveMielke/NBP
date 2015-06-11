package org.nbp.b2g.ui;

import android.util.Log;

public abstract class Keyboard {
  private final static String LOG_TAG = Keyboard.class.getName();

  private final static KeyboardInjector injector = Devices.keyboard.get();

  private static void logKeyboardEvent (int key, String action) {
    if (ApplicationSettings.LOG_ACTIONS) {
      Log.v(LOG_TAG, String.format("injecting scan code %s: %d", action, key));
    }
  }

  public static boolean pressKey (int key) {
    logKeyboardEvent(key, "press");
    return injector.keyboardPress(key);
  }

  public static boolean releaseKey (int key) {
    logKeyboardEvent(key, "release");
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

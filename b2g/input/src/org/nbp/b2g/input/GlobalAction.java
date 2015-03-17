package org.nbp.b2g.input;

import android.util.Log;

import android.view.KeyEvent;

public final class GlobalAction extends KeyCodeAction {
  private static final String LOG_TAG = GlobalAction.class.getName();

  protected final static int NULL_KEY_CODE = KeyEvent.KEYCODE_UNKNOWN;

  protected final int globalAction;

  @Override
  public final boolean performAction () {
    ScreenMonitor monitor = getScreenMonitor();

    if (monitor != null) {
      if (monitor.performGlobalAction(globalAction)) {
        return true;
      }
    }

    if (keyCode != NULL_KEY_CODE) {
      if (sendKey()) {
        return true;
      }
    }

    return false;
  }

  public GlobalAction (int globalAction, String name, int keyCode) {
    super(keyCode, ("GLOBAL_" + name));
    this.globalAction = globalAction;
  }

  public static void add (int keyMask, int globalAction, String name, int keyCode) {
    add(keyMask, new GlobalAction(globalAction, name, keyCode));
  }

  public static void add (int keyMask, int globalAction, String name) {
    add(keyMask, new GlobalAction(globalAction, name, NULL_KEY_CODE));
  }
}

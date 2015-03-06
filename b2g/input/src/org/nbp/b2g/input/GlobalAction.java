package org.nbp.b2g.input;

import android.util.Log;

import android.view.KeyEvent;

public final class GlobalAction extends KeyAction {
  private static final String LOG_TAG = GlobalAction.class.getName();

  protected final int globalAction;

  @Override
  public final boolean performAction () {
    ScreenMonitor monitor = ScreenMonitor.getScreenMonitor();

    if (monitor != null) {
      if (monitor.performGlobalAction(globalAction)) {
        return true;
      }
    }

    return super.performAction();
  }

  public GlobalAction (int globalAction, String name, int keyCode) {
    super(keyCode, name);
    this.globalAction = globalAction;
  }

  public static void add (int keyMask, int globalAction, String name, int keyCode) {
    add(keyMask, new GlobalAction(globalAction, name, keyCode));
  }

  public GlobalAction (int globalAction, String name) {
    this(globalAction, name, KeyEvent.KEYCODE_UNKNOWN);
  }

  public static void add (int keyMask, int globalAction, String name) {
    add(keyMask, new GlobalAction(globalAction, name));
  }
}

package org.nbp.b2g.input;

import android.view.KeyEvent;

public class GlobalAction extends ScanCodeAction {
  public static final int NULL_GLOBAL_ACTION = 0;

  protected int getGlobalAction () {
    return NULL_GLOBAL_ACTION;
  }

  @Override
  public final boolean performAction () {
    int globalAction = getGlobalAction();

    if (globalAction != NULL_GLOBAL_ACTION) {
      ScreenMonitor monitor = getScreenMonitor();

      if (monitor != null) {
        if (monitor.performGlobalAction(globalAction)) {
          return true;
        }
      }
    }

    return super.performAction();
  }

  public GlobalAction () {
    super();
  }
}

package org.nbp.b2g.ui;

import android.view.KeyEvent;

public abstract class GlobalAction extends ScanCodeAction {
  public final static int NULL_GLOBAL_ACTION = 0;

  protected int getGlobalAction () {
    return NULL_GLOBAL_ACTION;
  }

  @Override
  public boolean performAction () {
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

  protected GlobalAction (boolean isForDevelopers) {
    super(isForDevelopers);
  }
}

package org.nbp.b2g.ui;

import android.content.Context;

public abstract class ApplicationHooks {
  public static Context getContext () {
    Context context;
    if ((context = ScreenMonitor.getScreenMonitor()) != null) return context;
    if ((context = InputService.getInputService()) != null) return context;
    return null;
  }

  private ApplicationHooks () {
  }
}

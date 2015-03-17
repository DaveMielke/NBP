package org.nbp.b2g.input;

import android.os.SystemClock;
import android.view.ViewConfiguration;

public class ApplicationUtilities {
  public static void sleep (long duration) {
    SystemClock.sleep(duration);
  }

  public static long getLongPressTimeout () {
    return ViewConfiguration.getLongPressTimeout();
  }

  public static long getGlobalActionTimeout () {
    return ViewConfiguration.getGlobalActionKeyTimeout();
  }

  private ApplicationUtilities () {
  }
}

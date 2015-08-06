package org.nbp.b2g.ui;

import android.content.Context;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.graphics.PixelFormat;

public abstract class BrailleWindow {
  private static ViewGroup window = null;

  private final static WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
    WindowManager.LayoutParams.WRAP_CONTENT,
    WindowManager.LayoutParams.WRAP_CONTENT,
    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    PixelFormat.TRANSLUCENT
  );

  public static boolean start () {
    synchronized (parameters) {
      if (window == null) {
        Context context = ApplicationContext.getContext();
        if (context == null) return false;

        window = new LinearLayout(context);
        window.setBackgroundColor(0X88FF0000);
      }
    }

    WindowManager wm = ApplicationContext.getWindowManager();
    if (wm == null) return false;

    synchronized (wm) {
      wm.addView(window, parameters);
    }

    return true;
  }

  public static boolean stop () {
    WindowManager wm = ApplicationContext.getWindowManager();
    if (wm == null) return false;

    synchronized (wm) {
      wm.removeView(window);
    }

    return true;
  }

  private BrailleWindow () {
  }
}

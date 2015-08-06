package org.nbp.b2g.ui;

import android.content.Context;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.graphics.PixelFormat;

public class SystemWindow {
  private final WindowManager windowManager;
  protected final LinearLayout window;

  private final static WindowManager.LayoutParams layoutParameters = new WindowManager.LayoutParams(
    WindowManager.LayoutParams.WRAP_CONTENT,
    WindowManager.LayoutParams.WRAP_CONTENT,
    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    PixelFormat.TRANSLUCENT
  );

  public final void start () {
    synchronized (layoutParameters) {
      windowManager.addView(window, layoutParameters);
    }
  }

  public final void stop () {
    synchronized (layoutParameters) {
      windowManager.removeView(window);
    }
  }

  public SystemWindow (Context context) {
    windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    window = new LinearLayout(context);
  }
}

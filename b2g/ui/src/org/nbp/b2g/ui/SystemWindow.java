package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.view.WindowManager;

import android.widget.LinearLayout;
import android.graphics.PixelFormat;

import android.os.Looper;
import android.os.Handler;
import android.os.Message;

public class SystemWindow {
  private final static String LOG_TAG = SystemWindow.class.getName();

  private final WindowManager windowManager;
  private final Thread windowThread;

  protected final class WindowLayout extends LinearLayout {
    public WindowLayout (Context context) {
      super(context);
    }
  }

  private WindowLayout windowLayout = null;
  private Handler windowHandler = null;

  private final static WindowManager.LayoutParams windowParameters = new WindowManager.LayoutParams(
    WindowManager.LayoutParams.WRAP_CONTENT /* width */,
    WindowManager.LayoutParams.WRAP_CONTENT /* height */,
    0 /* x */,
    0 /* y */,
    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    PixelFormat.TRANSLUCENT
  );

  public final void logProperties () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        StringBuilder sb = new StringBuilder();

        sb.append("system window: ");
        sb.append(SystemWindow.this.getClass().getSimpleName());
        sb.append(':');

        sb.append(" Height:");
        sb.append(windowLayout.getHeight());

        sb.append(" Width:");
        sb.append(windowLayout.getWidth());

        sb.append(" Alpha:");
        sb.append(windowLayout.getAlpha());

        Log.d(LOG_TAG, sb.toString());
      }
    });
  }

  protected final WindowLayout getWindowLayout () {
    return windowLayout;
  }

  protected final void runOnWindowThread (Runnable runnable) {
    windowHandler.post(runnable);
  }

  private boolean hasParent () {
    return windowLayout.getParent() != null;
  }

  public final void setWindowVisible () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        if (!hasParent()) windowManager.addView(windowLayout, windowParameters);
      }
    });
  }

  public final void setWindowInvisible () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        if (hasParent()) windowManager.removeView(windowLayout);
      }
    });
  }

  public void setWindowVisibility (boolean visible) {
   if (visible) {
     setWindowVisible();
   } else {
     setWindowInvisible();
   }
  }

  public SystemWindow (final Context context) {
    windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

    windowThread = new Thread() {
      @Override
      public void run () {
        Looper.prepare();
        windowLayout = new WindowLayout(context);

        windowHandler = new Handler() {
          @Override
          public void handleMessage (Message msg) {
          }
        };

        synchronized (this) {
          notify();
        }

        Looper.loop();
      }
    };

    synchronized (windowThread) {
      windowThread.start();

      try {
        windowThread.wait();
      } catch (InterruptedException exception) {
      }
    }
  }
}

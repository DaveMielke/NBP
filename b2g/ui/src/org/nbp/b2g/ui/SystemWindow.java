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

  protected LinearLayout windowView = null;
  private Handler windowHandler = null;

  private final static WindowManager.LayoutParams windowLayout = new WindowManager.LayoutParams(
    WindowManager.LayoutParams.WRAP_CONTENT, // width
    WindowManager.LayoutParams.WRAP_CONTENT, // height
    0, 0, // x, y
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
        sb.append(windowView.getHeight());

        sb.append(" Width:");
        sb.append(windowView.getWidth());

        sb.append(" Alpha:");
        sb.append(windowView.getAlpha());

        Log.d(LOG_TAG, sb.toString());
      }
    });
  }

  protected final void runOnWindowThread (Runnable runnable) {
    windowHandler.post(runnable);
  }

  private boolean hasParent () {
    return windowView.getParent() != null;
  }

  public final void setVisible () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        if (!hasParent()) windowManager.addView(windowView, windowLayout);
      }
    });
  }

  public final void setInvisible () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        if (hasParent()) windowManager.removeView(windowView);
      }
    });
  }

  public void setVisibility (boolean visible) {
   if (visible) {
     setVisible();
   } else {
     setInvisible();
   }
  }

  public SystemWindow (final Context context) {
    windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

    windowThread = new Thread() {
      @Override
      public void run () {
        Looper.prepare();
        windowView = new LinearLayout(context);

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

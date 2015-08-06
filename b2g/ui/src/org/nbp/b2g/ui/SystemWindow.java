package org.nbp.b2g.ui;

import android.content.Context;
import android.view.WindowManager;

import android.widget.LinearLayout;
import android.graphics.PixelFormat;

import android.os.Looper;
import android.os.Handler;
import android.os.Message;

public class SystemWindow {
  private final WindowManager windowManager;
  private final Thread windowThread;

  protected LinearLayout windowView = null;
  private Handler windowHandler = null;

  private final static WindowManager.LayoutParams layoutParameters = new WindowManager.LayoutParams(
    WindowManager.LayoutParams.WRAP_CONTENT,
    WindowManager.LayoutParams.WRAP_CONTENT,
    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    PixelFormat.TRANSLUCENT
  );

  protected void runOnWindowThread (Runnable runnable) {
    windowHandler.post(runnable);
  }

  public final void start () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        windowManager.addView(windowView, layoutParameters);
      }
    });
  }

  public final void stop () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        windowManager.removeView(windowView);
      }
    });
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

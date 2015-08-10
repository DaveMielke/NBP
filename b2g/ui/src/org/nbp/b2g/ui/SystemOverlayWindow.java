package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.view.WindowManager;

import android.widget.LinearLayout;
import android.graphics.PixelFormat;

import android.os.Looper;
import android.os.Handler;
import android.os.Message;

public class SystemOverlayWindow {
  private final static String LOG_TAG = SystemOverlayWindow.class.getName();

  private final WindowManager windowManager;
  private final Thread windowThread;

  protected final class WindowLayout extends LinearLayout {
    public WindowLayout (Context context) {
      super(context);
    }
  }

  private final ThreadLocal<WindowLayout> windowLayout = new ThreadLocal<WindowLayout>();
  private Handler windowHandler = null;

  private final static WindowManager.LayoutParams windowParameters = new WindowManager.LayoutParams(
    WindowManager.LayoutParams.WRAP_CONTENT /* width */,
    WindowManager.LayoutParams.WRAP_CONTENT /* height */,
    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    PixelFormat.TRANSLUCENT
  );

  protected final WindowLayout getLayout () {
    return windowLayout.get();
  }

  public final void logProperties () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        WindowLayout layout = getLayout();
        StringBuilder sb = new StringBuilder();

        sb.append("system window: ");
        sb.append(SystemOverlayWindow.this.getClass().getSimpleName());
        sb.append(':');

        sb.append(" Height:");
        sb.append(layout.getHeight());

        sb.append(" Width:");
        sb.append(layout.getWidth());

        sb.append(" Alpha:");
        sb.append(layout.getAlpha());

        Log.d(LOG_TAG, sb.toString());
      }
    });
  }

  protected final void setGravity (int gravity) {
    windowParameters.gravity = gravity;
  }

  protected final void runOnWindowThread (Runnable runnable) {
    windowHandler.post(runnable);
  }

  private static boolean isVisible (WindowLayout layout) {
    return layout.getParent() != null;
  }

  public final void setVisible () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        WindowLayout layout = getLayout();

        if (!isVisible(layout)) {
          windowManager.addView(layout, windowParameters);
          Log.d(LOG_TAG, "window now visible");
        }
      }
    });
  }

  public final void setInvisible () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        WindowLayout layout = getLayout();

        if (isVisible(layout)) {
          windowManager.removeView(layout);
          Log.d(LOG_TAG, "window now invisible");
        }
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

  public SystemOverlayWindow (final Context context) {
    windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

    windowThread = new Thread() {
      @Override
      public void run () {
        Looper.prepare();
        windowLayout.set(new WindowLayout(context));

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

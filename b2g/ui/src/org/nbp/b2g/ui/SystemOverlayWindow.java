package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.view.WindowManager;

import android.widget.LinearLayout;
import android.graphics.PixelFormat;

import android.view.Display;
import android.graphics.Point;

import android.os.Looper;
import android.os.Handler;
import android.os.Message;

public class SystemOverlayWindow {
  private final static String LOG_TAG = SystemOverlayWindow.class.getName();

  protected final String getWindowType () {
    return getClass().getSimpleName();
  }

  private final void logWindowState (String state) {
    Log.d(LOG_TAG, String.format("window now %s: %s", state, getWindowType()));
  }

  private final WindowManager windowManager;
  private final Thread windowThread;

  public final void logDisplayProperties () {
    Display display = windowManager.getDefaultDisplay();

    Point size = new Point();
    display.getSize(size);

    Log.d(LOG_TAG,
      String.format(
        "display properties: %dx%d", size.x, size.y
      )
    );
  }

  protected final static class WindowParameters extends WindowManager.LayoutParams {
    public WindowParameters () {
      super(
        WindowManager.LayoutParams.WRAP_CONTENT /* width */,
        WindowManager.LayoutParams.WRAP_CONTENT /* height */,
        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,

        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,

        PixelFormat.TRANSLUCENT
      );
    }
  }

  private final static WindowParameters windowParameters = new WindowParameters();
  protected void adjustWindowParameters (WindowParameters parameters) {}

  protected final class WindowLayout extends LinearLayout {
    public WindowLayout (Context context) {
      super(context);
    }
  }

  private final ThreadLocal<WindowLayout> windowLayout = new ThreadLocal<WindowLayout>();
  private Handler windowHandler = null;

  protected final WindowLayout getLayout () {
    return windowLayout.get();
  }

  protected final void runOnWindowThread (Runnable runnable) {
    windowHandler.post(runnable);
  }

  private static boolean isVisible (WindowLayout layout) {
    return layout.getParent() != null;
  }

  public final void setVisible () {
    runOnWindowThread(
      new Runnable() {
        @Override
        public void run () {
          WindowLayout layout = getLayout();

          if (!isVisible(layout)) {
            adjustWindowParameters(windowParameters);
            windowManager.addView(layout, windowParameters);
            logWindowState("visible");
          }
        }
      }
    );
  }

  public final void setInvisible () {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        WindowLayout layout = getLayout();

        if (isVisible(layout)) {
          windowManager.removeView(layout);
          logWindowState("invisible");
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
    logDisplayProperties();

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

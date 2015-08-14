package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.view.WindowManager;

import android.widget.LinearLayout;
import android.graphics.PixelFormat;
import android.view.Gravity;

import android.view.Display;
import android.graphics.Point;

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
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    PixelFormat.TRANSLUCENT
  );

  protected final WindowLayout getLayout () {
    return windowLayout.get();
  }

  private final void setPosition () {
    Display display = windowManager.getDefaultDisplay();

    Point screenSize = new Point();
    display.getRealSize(screenSize);

    Log.d(LOG_TAG, String.format(
      "screen size: %dx%d", screenSize.x, screenSize.y
    ));

    Point windowSize = new Point();
    display.getSize(windowSize);

    Log.d(LOG_TAG, String.format(
      "window size: %dx%d", windowSize.x, windowSize.y
    ));

    windowParameters.gravity = Gravity.BOTTOM;
    windowParameters.y = windowSize.y - screenSize.y;

    Log.d(LOG_TAG, "bottom adjustment: " + windowParameters.y);
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
    setPosition();

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

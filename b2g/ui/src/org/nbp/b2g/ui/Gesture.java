package org.nbp.b2g.ui;

import android.util.Log;

public abstract class Gesture {
  private final static String LOG_TAG = Gesture.class.getName();

  private final static GestureInjector injector = Devices.pointer.get();

  private static boolean begin (int x, int y) {
    Log.v(LOG_TAG, String.format("gesture begin: %dx%d", x, y));

    if (injector == null) return true;
    return injector.gestureBegin(x, y);
  }

  private static boolean end () {
    Log.v(LOG_TAG, "gesture end");

    if (injector == null) return true;
    return injector.gestureEnd();
  }

  private static boolean move (int x, int y) {
    Log.v(LOG_TAG, String.format("gesture move: %dx%d", x, y));

    if (injector == null) return true;
    return injector.gestureMove(x, y);
  }


  public static boolean tap (int x, int y, int count) {
    if (count < 1) return false;
    if (ApplicationContext.isTouchExplorationActive()) count += 1;

    while (true) {
      if (!begin(x, y)) return false;
      ApplicationUtilities.sleep(45);
      if (!end()) return false;
      if ((count -= 1) == 0) return true;
      ApplicationUtilities.sleep(100);
    }
  }

  public static boolean tap (int x, int y) {
    return tap(x, y, 1);
  }

  public static boolean swipe (int x1, int y1, int x2, int y2) {
    double horizontalPosition = (double)x1;
    double verticalPosition = (double)y1;

    double horizontalDistance = (double)x2 - horizontalPosition;
    double verticalDistance = (double)y2 - verticalPosition;

    double totalDistance = Math.hypot(horizontalDistance, verticalDistance);
    double stepDistance = 10.0;
    double stepCount = Math.rint(totalDistance / stepDistance);

    double horizontalStep = horizontalDistance / stepCount;
    double verticalStep = verticalDistance / stepCount;

    if (!begin(x1, y1)) return false;
    int stepInterval = 10;

    while (true) {
      ApplicationUtilities.sleep(stepInterval);
      if (stepCount < 1.5) break;
      stepCount -= 1.0;

      horizontalPosition += horizontalStep;
      verticalPosition += verticalStep;

      int x = (int)Math.rint(horizontalPosition);
      int y = (int)Math.rint(verticalPosition);
      if (!move(x, y)) return false;
    }

    if (!move(x2, y2)) return false;
    return end();
  }

  private Gesture () {
  }
}

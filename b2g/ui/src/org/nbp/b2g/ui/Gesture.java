package org.nbp.b2g.ui;

import android.util.Log;

public abstract class Gesture {
  private final static String LOG_TAG = Gesture.class.getName();

  private final static GestureInjector injector = Devices.motion.get();
  private static long startTime = 0;

  private static void setStartTime () {
    startTime = ApplicationUtilities.getSystemClock();
  }

  private static void sleep (long duration) {
    duration -= ApplicationUtilities.getSystemClock() - startTime;
    ApplicationUtilities.sleep(duration);
    setStartTime();
  }

  private static boolean begin (int x, int y, int fingers) {
    Log.v(LOG_TAG, String.format(
      "gesture begin: [%d,%d] Fingers:%d", x, y, fingers
    ));

    setStartTime();
    if (injector == null) return true;
    return injector.gestureBegin(x, y, fingers);
  }

  private static boolean move (int x, int y) {
    Log.v(LOG_TAG, String.format("gesture move: [%d,%d]", x, y));
    if (injector == null) return true;
    return injector.gestureMove(x, y);
  }

  private static boolean end () {
    Log.v(LOG_TAG, "gesture end");
    if (injector == null) return true;
    return injector.gestureEnd();
  }

  private static boolean end (int x, int y) {
    Log.v(LOG_TAG, String.format("gesture end: [%d,%d]", x, y));
    if (injector == null) return true;
    return injector.gestureEnd(x, y);
  }

  public static boolean isEnabled () {
    return injector.gestureEnabled();
  }

  public static void tap (final int x, final int y, final int count) {
    if (count < 1) {
      throw new IllegalArgumentException(String.format(
        "count must be greater than 0: %d", count
      ));
    }

    Runnable runnable = new Runnable() {
      @Override
      public void run () {
        int tapsLeft = count;

        if (ApplicationContext.isTouchExplorationActive()) {
          tapsLeft += 1;
        }

        while (true) {
          if (!begin(x, y, 1)) return;
          sleep(ApplicationParameters.TAP_HOLD_TIME);
          if (!end()) return;
          if ((tapsLeft -= 1) == 0) break;
          sleep(ApplicationParameters.TAP_WAIT_TIME);
        }
      }
    };

    ApplicationUtilities.run(runnable);
  }

  public static void tap (int x, int y) {
    tap(x, y, 1);
  }

  public static void swipe (
    final int x1, final int y1,
    final int x2, final int y2,
    final int fingers
  ) {
    if (fingers < 1) {
      throw new IllegalArgumentException(String.format(
        "fingers must be greater than 0: %d", fingers
      ));
    }

    Runnable runnable = new Runnable() {
      @Override
      public void run () {
        int fingerCount = fingers;

        if (ApplicationContext.isTouchExplorationActive()) {
          fingerCount += 1;
        }

        double horizontalPosition = (double)x1;
        double verticalPosition = (double)y1;

        double horizontalDistance = (double)x2 - horizontalPosition;
        double verticalDistance = (double)y2 - verticalPosition;

        double totalDistance = Math.hypot(horizontalDistance, verticalDistance);
        double stepDistance = ApplicationParameters.SWIPE_STEP_DISTANCE;
        double stepCount = Math.rint(totalDistance / stepDistance);

        double horizontalStep = horizontalDistance / stepCount;
        double verticalStep = verticalDistance / stepCount;

        if (!begin(x1, y1, fingerCount)) return;
        long stepInterval = ApplicationParameters.SWIPE_STEP_INTERVAL;

        while (true) {
          sleep(stepInterval);
          if (stepCount < 1.5) break;
          stepCount -= 1.0;

          horizontalPosition += horizontalStep;
          verticalPosition += verticalStep;

          int x = (int)Math.rint(horizontalPosition);
          int y = (int)Math.rint(verticalPosition);
          if (!move(x, y)) return;
        }

        end(x2, y2);
      }
    };

    ApplicationUtilities.run(runnable);
  }

  public static void swipe (int x1, int y1, int x2, int y2) {
    swipe(x1, y1, x2, y2, 1);
  }

  private Gesture () {
  }
}

package org.nbp.b2g.ui;

public abstract class Gesture {
  private final static GestureInjector injector = Devices.pointer.get();

  public static boolean tap (int x, int y, int count) {
    if (count < 1) return false;
    if (ApplicationContext.isTouchExplorationActive()) count += 1;

    while (true) {
      if (!injector.gestureBegin(x, y)) return false;
      ApplicationUtilities.sleep(45);
      if (!injector.gestureEnd()) return false;
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
    double stepCount = totalDistance / stepDistance;

    double horizontalStep = horizontalDistance / stepCount;
    double verticalStep = verticalDistance / stepCount;

    if (!injector.gestureBegin(x1, y1)) return false;
    int stepInterval = 10;

    while (stepCount > 1.0) {
      stepCount -= 1.0;
      ApplicationUtilities.sleep(stepInterval);

      horizontalPosition += horizontalStep;
      verticalPosition += verticalStep;

      int x = (int)Math.round(horizontalPosition);
      int y = (int)Math.round(verticalPosition);
      if (!injector.gestureMove(x, y)) return false;
    }

    if (!injector.gestureMove(x2, y2)) return false;
    return injector.gestureEnd();
  }

  private Gesture () {
  }
}

package org.nbp.b2g.ui;

public abstract class Gesture {
  private final static GestureInjecter injecter = Devices.getTouchDevice();

  public static boolean tap (int x, int y, int count) {
    if (count < 1) return false;
    if (ApplicationContext.isTouchExplorationActive()) count += 1;

    while (true) {
      if (!injecter.gestureBegin(x, y)) return false;
      ApplicationUtilities.sleep(45);
      if (!injecter.gestureEnd()) return false;
      if ((count -= 1) == 0) return true;
      ApplicationUtilities.sleep(100);
    }
  }

  public static boolean tap (int x, int y) {
    return tap(x, y, 1);
  }

  public static boolean swipe (int x1, int y1, int x2, int y2) {
    if (injecter.gestureBegin(x1, y1)) {
      if (injecter.gestureMove(x2, y2)) {
        if (injecter.gestureEnd()) {
          return true;
        }
      }
    }

    return false;
  }

  private Gesture () {
  }
}

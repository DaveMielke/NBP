package org.nbp.b2g.ui;

public interface GestureInjector {
  public boolean gestureBegin (int x, int y, int fingers);
  public boolean gestureEnd ();
  public boolean gestureMove (int x, int y);
}

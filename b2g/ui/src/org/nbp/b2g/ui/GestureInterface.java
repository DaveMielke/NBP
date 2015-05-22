package org.nbp.b2g.ui;

public interface GestureInterface {
  public boolean gestureBegin (int x, int y);
  public boolean gestureEnd ();
  public boolean gestureMove (int x, int y);
}

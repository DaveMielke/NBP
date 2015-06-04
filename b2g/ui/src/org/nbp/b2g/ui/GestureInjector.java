package org.nbp.b2g.ui;

public interface GestureInjector {
  public boolean gestureEnabled ();
  public boolean gestureBegin (int x, int y, int fingers);
  public boolean gestureMove (int x, int y);
  public boolean gestureEnd ();
  public boolean gestureEnd (int x, int y);
}

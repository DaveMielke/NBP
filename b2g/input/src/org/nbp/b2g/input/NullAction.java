package org.nbp.b2g.input;

public final class NullAction extends Action {
  @Override
  public final boolean performAction () {
    return true;
  }

  public NullAction () {
    super("null");
  }

  public static void add (int keyMask) {
    add(keyMask, new NullAction());
  }
}

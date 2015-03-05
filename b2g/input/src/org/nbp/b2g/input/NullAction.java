package org.nbp.b2g.input;

public final class NullAction extends Action {
  @Override
  public String getActionName () {
    return "null";
  }

  @Override
  public final boolean performAction () {
    return true;
  }

  public NullAction () {
    super();
  }

  public static void add (int keyMask) {
    add(keyMask, new NullAction());
  }
}

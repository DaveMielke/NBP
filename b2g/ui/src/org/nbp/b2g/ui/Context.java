package org.nbp.b2g.ui;

public class Context {
  private static Context currentContext = new HostContext();
  private static Object LOCK = new Object();

  public static Context getCurrentContext () {
    synchronized (LOCK) {
      return currentContext;
    }
  }

  private final KeyBindings keyBindings;

  public KeyBindings getKeyBindings () {
    return keyBindings;
  }

  public Context (String[] keysFileNames) {
    keyBindings = new KeyBindings(keysFileNames);
  }
}

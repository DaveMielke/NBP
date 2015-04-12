package org.nbp.b2g.ui;

public class KeyboardMonitor extends EventMonitor {
  private final static Object LOCK = new Object();
  private static KeyboardMonitor keyboardMonitor = null;

  @Override
  protected native int openDevice ();

  public static KeyboardMonitor getKeyboardMonitor () {
    synchronized (LOCK) {
      if (keyboardMonitor == null) keyboardMonitor = new KeyboardMonitor();
      return keyboardMonitor;
    }
  }

  public static boolean isActive () {
    return getKeyboardMonitor().isAlive();
  }

  public KeyboardMonitor () {
    super("keyboard-monitor");
  }
}

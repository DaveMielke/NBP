package org.nbp.b2g.ui;

public class KeyboardMonitor extends EventMonitor {
  private final static Object LOCK = new Object();
  private static KeyboardMonitor keyboardMonitor = null;

  public static KeyboardMonitor getKeyboardMonitor () {
    synchronized (LOCK) {
      if (keyboardMonitor == null) keyboardMonitor = new KeyboardMonitor();
      return keyboardMonitor;
    }
  }

  public static boolean isActive () {
    return getKeyboardMonitor().isAlive();
  }

  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.START_KEYBOARD_MONITOR;
  }

  @Override
  protected native int openDevice ();

  public KeyboardMonitor () {
    super("keyboard-monitor");
  }
}

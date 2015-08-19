package org.nbp.b2g.ui;

public class KeyboardMonitor extends EventMonitor {
  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.ENABLE_KEYBOARD_MONITOR;
  }

  @Override
  protected native int openDevice ();

  @Override
  protected boolean handleKeyEvent (int code, boolean press) {
    switch (code) {
      case ScanCode.WAKEUP:
        Keyboard.injectKey(code, press);
        return true;

      default:
        return false;
    }
  }

  public KeyboardMonitor () {
    super("keyboard-monitor");
  }

  static {
    ApplicationUtilities.loadLibrary();
  }
}

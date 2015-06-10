package org.nbp.b2g.ui;

public class KeyboardMonitor extends EventMonitor {
  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.ENABLE_KEYBOARD_MONITOR;
  }

  @Override
  protected native int openDevice ();

  @Override
  public void onKeyEvent (int code, boolean press) {
    switch (code) {
      case ScanCode.WAKEUP:
        Keyboard.injectKey(code, press);
        break;

      default:
        super.onKeyEvent(code, press);
        break;
    }
  }

  public KeyboardMonitor () {
    super("keyboard-monitor");
  }
}

package org.nbp.b2g.ui;

public class KeyboardMonitor extends EventMonitor {
  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.ENABLE_KEYBOARD_MONITOR;
  }

  @Override
  protected native int openDevice ();

  public KeyboardMonitor () {
    super("keyboard-monitor");
  }
}

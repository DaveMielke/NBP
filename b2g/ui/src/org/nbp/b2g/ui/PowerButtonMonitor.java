package org.nbp.b2g.ui;

public class PowerButtonMonitor extends EventMonitor {
  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.ENABLE_POWER_BUTTON_MONITOR;
  }

  @Override
  protected native int openDevice ();

  private final static int POWER_BUTTON_SCAN_CODE = Keyboard.getScanCodeValue("POWER");

  private static boolean isAwake = true;

  @Override
  protected boolean handleKeyEvent (int code, boolean press) {
    if (press) isAwake = ApplicationContext.isAwake();

    Keyboard.injectKey(code, press);
    Controls.restoreSaneValues();

    int keyMask = isAwake? KeyMask.SLEEP: KeyMask.WAKE;
    KeyEvents.handleNavigationKeyEvent(keyMask, press);

    return true;
  }

  public PowerButtonMonitor () {
    super("power-button-monitor");
  }

  static {
    ApplicationUtilities.loadLibrary();
  }
}

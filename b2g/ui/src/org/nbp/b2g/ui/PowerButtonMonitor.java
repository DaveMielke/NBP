package org.nbp.b2g.ui;

public class PowerButtonMonitor extends EventMonitor {
  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.ENABLE_POWER_BUTTON_MONITOR;
  }

  @Override
  protected native int openDevice ();

  private final static int POWER_BUTTON_SCAN_CODE = KeyboardDevice.getScanCode("POWER");

  private static boolean wasAwake = true;

  @Override
  protected boolean handleKeyEvent (int code, boolean press) {
    if (press) wasAwake = ApplicationContext.isAwake();
    Keyboard.injectKey(code, press);
    Controls.getOneHandControl().previousValue();

    int keyMask = wasAwake? KeyMask.POWER_OFF: KeyMask.POWER_ON;
    KeyEvents.handleNavigationKeyEvent(keyMask, press);

    return true;
  }

  public PowerButtonMonitor () {
    super("power-button-monitor");
  }
}

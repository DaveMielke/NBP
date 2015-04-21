package org.nbp.b2g.ui;

public class PowerButtonMonitor extends EventMonitor {
  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.ENABLE_POWER_BUTTON_MONITOR;
  }

  @Override
  protected native int openDevice ();

  private final static int POWER_BUTTON_SCAN_CODE = KeyboardDevice.getScanCode("POWER");

  @Override
  public void onKeyEvent (int code, boolean press) {
    Devices.getKeyboardDevice().sendKeyEvent(code, press);
    ApplicationParameters.CURRENT_ONE_HAND = false;
    super.onKeyEvent(code, press);
  }

  public PowerButtonMonitor () {
    super("power-button-monitor");
  }
}

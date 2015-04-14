package org.nbp.b2g.ui;

public class PowerButtonMonitor extends EventMonitor {
  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.START_POWER_BUTTON_MONITOR;
  }

  @Override
  protected native int openDevice ();

  private final static int POWER_BUTTON_SCAN_CODE = KeyboardDevice.getScanCode("POWER");

  @Override
  public void onKeyEvent (int code, boolean press) {
    ScanCodeAction.keyboardDevice.sendKey(code, press);
    ApplicationParameters.ONE_HAND_MODE = false;
    super.onKeyEvent(code, press);
  }

  public PowerButtonMonitor () {
    super("power-button-monitor");
  }
}

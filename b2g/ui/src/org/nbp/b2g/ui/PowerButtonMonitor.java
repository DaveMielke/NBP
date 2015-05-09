package org.nbp.b2g.ui;

public class PowerButtonMonitor extends EventMonitor {
  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.ENABLE_POWER_BUTTON_MONITOR;
  }

  @Override
  protected native int openDevice ();

  private final static int POWER_BUTTON_SCAN_CODE = KeyboardDevice.getScanCode("POWER");

  private static boolean wasOff = false;

  @Override
  public void onKeyEvent (int code, boolean press) {
    if (press) wasOff = !ApplicationContext.isScreenOn();
    Devices.getKeyboardDevice().sendKeyEvent(code, press);

    Controls.getOneHandControl().previousValue();
    if (wasOff) super.onKeyEvent(code, press);
  }

  public PowerButtonMonitor () {
    super("power-button-monitor");
  }
}

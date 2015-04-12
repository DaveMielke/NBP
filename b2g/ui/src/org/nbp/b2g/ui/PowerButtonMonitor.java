package org.nbp.b2g.ui;

public class PowerButtonMonitor extends EventMonitor {
  private final static Object LOCK = new Object();
  private static PowerButtonMonitor powerButtonMonitor = null;

  public static PowerButtonMonitor getPowerButtonMonitor () {
    synchronized (LOCK) {
      if (powerButtonMonitor == null) powerButtonMonitor = new PowerButtonMonitor();
      return powerButtonMonitor;
    }
  }

  public static boolean isActive () {
    return getPowerButtonMonitor().isAlive();
  }

  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.START_POWER_BUTTON_MONITOR;
  }

  @Override
  protected native int openDevice ();

  public PowerButtonMonitor () {
    super("power-button-monitor");
  }
}

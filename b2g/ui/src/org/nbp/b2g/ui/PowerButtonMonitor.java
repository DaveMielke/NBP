package org.nbp.b2g.ui;

public class PowerButtonMonitor extends EventMonitor {
  private final static Object LOCK = new Object();
  private static PowerButtonMonitor powerButtonMonitor = null;

  @Override
  protected native int openDevice ();

  public static PowerButtonMonitor getPowerButtonMonitor () {
    synchronized (LOCK) {
      if (powerButtonMonitor == null) powerButtonMonitor = new PowerButtonMonitor();
      return powerButtonMonitor;
    }
  }

  public static boolean isActive () {
    return getPowerButtonMonitor().isAlive();
  }

  public PowerButtonMonitor () {
    super("power-button-monitor");
  }
}

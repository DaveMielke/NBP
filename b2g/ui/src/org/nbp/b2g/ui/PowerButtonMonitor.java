package org.nbp.b2g.ui;

public class PowerButtonMonitor extends EventMonitor {
  @Override
  protected boolean isEnabled () {
    return ApplicationParameters.ENABLE_POWER_BUTTON_MONITOR;
  }

  @Override
  protected native int openDevice ();

  private static boolean isAwake = true;

  @Override
  protected boolean handleKeyEvent (int code, boolean press) {
    if (press) isAwake = ApplicationContext.isAwake();
    Keyboard.injectKey(code, press);

    if (press && !isAwake) {
      Controls.restoreSaneValues();
    }

    {
      int keyMask = isAwake? KeyMask.SLEEP: KeyMask.WAKE;
      KeyEvents.handleNavigationKeyEvent(keyMask, press);
    }

    if (ApplicationSettings.ONE_HAND && !press) {
      if (KeyEvents.getNavigationKeys() != 0) {
        KeyEvents.handleNavigationKey(KeyMask.SPACE);
      }
    }

    return true;
  }

  public PowerButtonMonitor () {
    super("power-button-monitor");
  }

  static {
    ApplicationUtilities.loadLibrary();
  }
}

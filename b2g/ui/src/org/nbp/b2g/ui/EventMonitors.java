package org.nbp.b2g.ui;

public abstract class EventMonitors {
  private final static KeyboardMonitor keyboardMonitor = new KeyboardMonitor();
  private final static PowerButtonMonitor powerButtonMonitor = new PowerButtonMonitor();

  private final static EventMonitor[] eventMonitors = new EventMonitor[] {
    keyboardMonitor,
    powerButtonMonitor
  };

  public static KeyboardMonitor getKeyboardMonitor () {
    return keyboardMonitor;
  }

  public static PowerButtonMonitor getPowerButtonMonitor () {
    return powerButtonMonitor;
  }

  public static void startEventMonitors () {
    KeyEvents.resetKeys();

    for (EventMonitor monitor : eventMonitors) {
      if (monitor.isEnabled()) monitor.start();
    }
  }

  private EventMonitors () {
  }
}

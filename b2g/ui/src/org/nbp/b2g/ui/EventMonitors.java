package org.nbp.b2g.ui;

public abstract class EventMonitors {
  public final static KeyboardMonitor keyboard = new KeyboardMonitor();

  private final static EventMonitor[] eventMonitors = new EventMonitor[] {
    keyboard
  };

  public static void startEventMonitors () {
    KeyEvents.resetKeys();

    for (EventMonitor monitor : eventMonitors) {
      if (monitor.isEnabled()) monitor.start();
    }
  }

  private EventMonitors () {
  }
}

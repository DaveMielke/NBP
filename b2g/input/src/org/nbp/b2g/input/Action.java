package org.nbp.b2g.input;

import java.util.Map;
import java.util.HashMap;

import android.inputmethodservice.InputMethodService;

public abstract class Action {
  private static Map<Integer, Action> actionMap = new HashMap<Integer, Action>();

  final String actionName;

  public abstract boolean performAction ();

  public String getName () {
    return actionName;
  }

  protected final InputService getInputService () {
    return InputService.getInputService();
  }

  protected final ScreenMonitor getScreenMonitor () {
    return ScreenMonitor.getScreenMonitor();
  }

  protected boolean delay (long interval) {
    try {
      Thread.sleep(interval);
    } catch (InterruptedException exception) {
      return false;
    }

    return true;
  }

  public Action (String name) {
    actionName = name;
  }

  public static void add (int keyMask, Action action) {
    actionMap.put(new Integer(keyMask), action);
  }

  public static Action getAction (int keyMask) {
    if (actionMap.size() == 0) Actions.add();
    return actionMap.get(new Integer(keyMask));
  }
}

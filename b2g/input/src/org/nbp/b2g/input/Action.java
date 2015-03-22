package org.nbp.b2g.input;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.inputmethodservice.InputMethodService;

public abstract class Action {
  private static final String LOG_TAG = MoveForwardAction.class.getName();

  private static Map<Integer, Action> actionMap = new HashMap<Integer, Action>();

  final String actionName;

  public abstract boolean performAction ();

  public String getName () {
    return actionName;
  }

  public boolean parseOperand (int keyMask, String operand) {
    Log.w(LOG_TAG, "invalid " + getName() + " operand: " + operand);
    return false;
  }

  protected final InputService getInputService () {
    return InputService.getInputService();
  }

  protected final ScreenMonitor getScreenMonitor () {
    return ScreenMonitor.getScreenMonitor();
  }

  protected Action (String name) {
    actionName = name;
  }

  public static void add (int keyMask, Action action) {
    actionMap.put(keyMask, action);
  }

  public static Action getAction (int keyMask) {
    if (actionMap.size() == 0) Actions.add();
    return actionMap.get(new Integer(keyMask));
  }
}

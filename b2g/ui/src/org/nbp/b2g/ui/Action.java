package org.nbp.b2g.ui;

import android.util.Log;

import android.view.inputmethod.InputConnection;

public abstract class Action {
  private final static String LOG_TAG = Action.class.getName();

  private final Endpoint endpoint;
  private final boolean isDeveloperAction;

  public Endpoint getEndpoint () {
    return endpoint;
  }

  public boolean isForDevelopers () {
    return isDeveloperAction;
  }

  public final String getName () {
    return getClass().getName();
  }

  public boolean performAction (int cursorKey) {
    return false;
  }

  public boolean performAction () {
    int[] cursorKeys = KeyEvents.getCursorKeys();
    if (cursorKeys == null) return false;
    if (cursorKeys.length != 1) return false;
    return performAction(cursorKeys[0]);
  }

  public boolean parseOperand (int keyMask, String operand) {
    Log.w(LOG_TAG, "invalid " + getName() + " operand: " + operand);
    return false;
  }

  protected void log (String message) {
    Log.v(LOG_TAG, message);
  }

  protected static int getNavigationKeys () {
    return KeyEvents.getNavigationKeys();
  }

  protected static boolean isChord () {
    return (getNavigationKeys() & KeyMask.SPACE) != 0;
  }

  protected final InputService getInputService () {
    return InputService.getInputService();
  }

  protected final InputConnection getInputConnection () {
    InputService service = getInputService();
    if (service == null) return null;

    InputConnection connection = service.getCurrentInputConnection();
    if (connection == null) Log.w(LOG_TAG, "no input connection");

    return connection;
  }

  protected Action (Endpoint endpoint, boolean isForDevelopers) {
    isDeveloperAction = isForDevelopers;
    this.endpoint = endpoint;
  }
}

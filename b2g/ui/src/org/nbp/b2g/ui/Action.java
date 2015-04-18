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
    return LanguageUtilities.getClassName(getClass());
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

  public boolean performAction (boolean isLongPress) {
    return performAction();
  }

  public boolean parseOperand (int keyMask, String operand) {
    Log.w(LOG_TAG, "invalid " + getName() + " operand: " + operand);
    return false;
  }

  protected void message (String text) {
    ApplicationUtilities.message(getEndpoint(), text);
  }

  protected void message (String label, boolean isOn) {
    message(label + " " + BooleanControl.getValue(isOn));
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

  protected Action (Endpoint endpoint, boolean isForDevelopers) {
    isDeveloperAction = isForDevelopers;
    this.endpoint = endpoint;
  }
}

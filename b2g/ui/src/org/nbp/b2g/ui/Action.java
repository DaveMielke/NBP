package org.nbp.b2g.ui;

import android.util.Log;

public abstract class Action {
  private final static String LOG_TAG = Action.class.getName();

  private final boolean isDeveloperAction;

  public boolean isForDevelopers () {
    return isDeveloperAction;
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

  public final String getName () {
    return getClass().getName();
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

  protected Action (boolean isForDevelopers) {
    isDeveloperAction = isForDevelopers;
  }
}

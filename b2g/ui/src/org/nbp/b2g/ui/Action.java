package org.nbp.b2g.ui;

import android.util.Log;

public abstract class Action {
  private final static String LOG_TAG = Action.class.getName();

  private final Endpoint endpoint;
  private final boolean isDeveloperAction;

  public final Endpoint getEndpoint () {
    return endpoint;
  }

  public final boolean isForDevelopers () {
    return isDeveloperAction;
  }

  public final String getName () {
    return LanguageUtilities.getClassName(getClass());
  }

  private String actionSummary = null;

  public final String getSummary () {
    synchronized (this) {
      if (actionSummary == null) {
        actionSummary = ApplicationContext.getString("action_summary_" + getName());
        if (actionSummary == null) actionSummary = "";
      }
    }

    return actionSummary;
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

  protected Integer getConfirmation () {
    return null;
  }

  protected final void log (String message) {
    Log.v(LOG_TAG, message);
  }

  protected final Action getAction (Class type) {
    return getEndpoint().getKeyBindings().getAction(type);
  }

  protected int getNavigationKeys () {
    return KeyEvents.getNavigationKeys();
  }

  protected final boolean isChord () {
    return (getNavigationKeys() & KeyMask.SPACE) != 0;
  }

  protected Action (Endpoint endpoint, boolean isForDevelopers) {
    isDeveloperAction = isForDevelopers;
    this.endpoint = endpoint;
  }
}

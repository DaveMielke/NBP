package org.nbp.b2g.ui;

import org.nbp.common.LanguageUtilities;

import android.util.Log;

public abstract class Action extends UserInterfaceComponent {
  private final static String LOG_TAG = Action.class.getName();

  private final Endpoint actionEndpoint;
  private final boolean isAdvancedAction;

  public final Endpoint getEndpoint () {
    return actionEndpoint;
  }

  public final boolean isAdvanced () {
    return isAdvancedAction;
  }

  public boolean editsInput () {
    return false;
  }

  public final String getName () {
    String name = LanguageUtilities.getClassName(getClass());

    int index = name.lastIndexOf('$');
    if (index >= 0) name = name.substring(index+1);

    return name;
  }

  private String actionSummary = null;

  public String getSummary () {
    synchronized (this) {
      if (actionSummary == null) {
        actionSummary = ApplicationContext.getString("action_summary_" + getName());
        if (actionSummary == null) actionSummary = "";
      }
    }

    return actionSummary;
  }

  public boolean isHidden () {
    return false;
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

  protected KeySet getNavigationKeys () {
    return KeyEvents.getNavigationKeys();
  }

  protected final boolean isChord () {
    return getNavigationKeys().get(KeySet.SPACE);
  }

  protected Action (Endpoint endpoint, boolean isAdvanced) {
    super();
    isAdvancedAction = isAdvanced;
    actionEndpoint = endpoint;
  }
}

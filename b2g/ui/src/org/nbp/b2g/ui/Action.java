package org.nbp.b2g.ui;

import java.io.File;

import android.util.Log;

import android.content.Context;

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

  public boolean performAction (int cursorKey) {
    return false;
  }

  public boolean performAction () {
    int[] cursorKeys = KeyEvents.getCursorKeys();
    if (cursorKeys == null) return false;
    if (cursorKeys.length != 1) return false;
    return performAction(cursorKeys[0]);
  }

  protected final void log (String message) {
    Log.v(LOG_TAG, message);
  }

  protected final Action getAction (Class type) {
    return getEndpoint().getKeyBindings().getAction(type);
  }

  protected static int getNavigationKeys () {
    return KeyEvents.getNavigationKeys();
  }

  protected static boolean isChord () {
    return (getNavigationKeys() & KeyMask.SPACE) != 0;
  }

  protected final File getActionDirectory () {
    Context context = ApplicationContext.getContext();
    if (context == null) return null;
    return context.getDir(getClass().getSimpleName(), Context.MODE_PRIVATE);
  }

  protected final File getActionFile (String name) {
    File directory = getActionDirectory();
    if (directory == null) return null;
    return new File(directory, name);
  }

  protected Action (Endpoint endpoint, boolean isForDevelopers) {
    isDeveloperAction = isForDevelopers;
    this.endpoint = endpoint;
  }
}

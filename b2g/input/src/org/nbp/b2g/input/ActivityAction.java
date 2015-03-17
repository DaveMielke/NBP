package org.nbp.b2g.input;

import android.content.Intent;

public final class ActivityAction extends Action {
  protected final Class activityClass;

  @Override
  public final boolean performAction () {
    InputService inputService = getInputService();

    if (inputService != null) {
      Intent intent = new Intent(inputService, activityClass);

      intent.addFlags(
        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
        Intent.FLAG_ACTIVITY_NO_HISTORY |
        Intent.FLAG_ACTIVITY_NEW_TASK |
        Intent.FLAG_ACTIVITY_SINGLE_TOP
      );

      inputService.startActivity(intent);
      return true;
    }

    return false;
  }

  public ActivityAction (Class activityClass) {
    super("ACTIVITY_" + activityClass.getName());
    this.activityClass = activityClass;
  }

  public static void add (int keyMask, Class activityClass) {
    add(keyMask, new ActivityAction(activityClass));
  }
}

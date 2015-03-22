package org.nbp.b2g.input;

import android.content.Context;
import android.content.Intent;

public class ActivityAction extends Action {
  protected final Class activityClass;

  @Override
  public final boolean performAction () {
    Context context = ApplicationHooks.getContext();

    if (context != null) {
      Intent intent = new Intent(context, activityClass);

      intent.addFlags(
        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
        Intent.FLAG_ACTIVITY_NO_HISTORY |
        Intent.FLAG_ACTIVITY_NEW_TASK |
        Intent.FLAG_ACTIVITY_SINGLE_TOP
      );

      context.startActivity(intent);
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

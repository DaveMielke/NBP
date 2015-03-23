package org.nbp.b2g.input;

import android.content.Context;
import android.content.Intent;

public class ActivityAction extends Action {
  protected Class getActivityClass () {
    return null;
  }

  @Override
  public final boolean performAction () {
    Class activityClass = getActivityClass();

    if (activityClass != null) {
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
    }

    return false;
  }

  public ActivityAction () {
    super();
  }
}

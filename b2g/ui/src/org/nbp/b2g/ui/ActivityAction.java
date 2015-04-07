package org.nbp.b2g.ui;

import android.content.Context;
import android.content.Intent;

public abstract class ActivityAction extends Action {
  protected Intent getIntent (Context context) {
    return null;
  }

  @Override
  public boolean performAction () {
    Context context = ApplicationHooks.getContext();
    if (context == null) return false;

    Intent intent = getIntent(context);
    if (intent == null) return false;

    intent.addFlags(
      Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
      Intent.FLAG_ACTIVITY_NO_HISTORY |
      Intent.FLAG_ACTIVITY_NEW_TASK |
      Intent.FLAG_ACTIVITY_SINGLE_TOP
    );

    context.startActivity(intent);
    return true;
  }

  protected ActivityAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}

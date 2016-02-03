package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import org.nbp.common.LaunchUtilities;

import android.content.Context;
import android.content.Intent;

public abstract class ActivityAction extends Action {
  protected abstract Intent getIntent (Context context);

  protected final Context getContext () {
    return ApplicationContext.getContext();
  }

  @Override
  public boolean performAction () {
    Context context = getContext();
    if (context == null) return false;

    Intent intent = getIntent(context);
    if (intent == null) return false;

    LaunchUtilities.launchActivity(intent);
    return true;
  }

  protected ActivityAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}

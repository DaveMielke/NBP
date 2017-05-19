package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import org.nbp.common.LaunchUtilities;

import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.content.ActivityNotFoundException;

public abstract class ActivityAction extends Action {
  private final static String LOG_TAG = ActivityAction.class.getName();

  protected abstract Intent getIntent (Context context);

  @Override
  public boolean performAction () {
    Context context = getContext();
    if (context == null) return false;

    Intent intent = getIntent(context);
    if (intent == null) return false;

    try {
      LaunchUtilities.launchActivity(intent);
      return true;
    } catch (ActivityNotFoundException exception) {
      Log.w(LOG_TAG, ("activity not found: " + exception.getMessage()));
    }

    return false;
  }

  protected ActivityAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}

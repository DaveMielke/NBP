package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;

public abstract class InternalActivityAction extends ActivityAction {
  protected abstract Class getActivityClass ();

  @Override
  protected Intent getIntent (Context context) {
    Intent intent = new Intent(context, getActivityClass());

    intent.addFlags(
      Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
      Intent.FLAG_ACTIVITY_NEW_TASK
    );

    return intent;
  }

  protected InternalActivityAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}

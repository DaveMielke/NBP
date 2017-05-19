package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import org.nbp.common.LaunchUtilities;

import android.content.Context;
import android.content.Intent;

public abstract class InternalActivityAction extends ActivityAction {
  protected abstract Class getActivityClass ();

  @Override
  protected Intent getIntent (Context context) {
    return LaunchUtilities.toIntent(getActivityClass());
  }

  protected InternalActivityAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}

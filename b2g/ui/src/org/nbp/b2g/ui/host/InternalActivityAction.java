package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;

public abstract class InternalActivityAction extends ActivityAction {
  protected abstract Class getActivityClass ();

  @Override
  protected Intent getIntent (Context context) {
    return ApplicationContext.toIntent(getActivityClass());
  }

  protected InternalActivityAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}

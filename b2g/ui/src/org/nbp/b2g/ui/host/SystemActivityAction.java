package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;

public abstract class SystemActivityAction extends ActivityAction {
  protected abstract String getIntentAction ();

  @Override
  protected Intent getIntent (Context context) {
    Intent intent = new Intent(getIntentAction());

    return intent;
  }

  protected SystemActivityAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}

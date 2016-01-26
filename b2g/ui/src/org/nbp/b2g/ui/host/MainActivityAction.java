package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;

public abstract class MainActivityAction extends ActivityAction {
  protected abstract String getCategory ();

  @Override
  protected Intent getIntent (Context context) {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(getCategory());
    return intent;
  }

  protected MainActivityAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}

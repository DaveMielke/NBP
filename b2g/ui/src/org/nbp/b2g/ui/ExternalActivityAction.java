package org.nbp.b2g.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public abstract class ExternalActivityAction extends ActivityAction {
  protected abstract String getPackageName ();

  @Override
  protected Intent getIntent (Context context) {
    return context.getPackageManager().getLaunchIntentForPackage(getPackageName());
  }

  protected ExternalActivityAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}

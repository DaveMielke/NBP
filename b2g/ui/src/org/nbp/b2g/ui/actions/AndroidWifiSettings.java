package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;

public class AndroidWifiSettings extends ActivityAction {
  @Override
  protected Intent getIntent (Context context) {
    return new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
  }

  public AndroidWifiSettings () {
    super(false);
  }
}

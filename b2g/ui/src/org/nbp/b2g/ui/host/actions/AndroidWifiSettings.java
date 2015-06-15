package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class AndroidWifiSettings extends SystemActivityAction {
  @Override
  protected String getIntentAction () {
    return android.provider.Settings.ACTION_WIFI_SETTINGS;
  }

  public AndroidWifiSettings (Endpoint endpoint) {
    super(endpoint, false);
  }
}

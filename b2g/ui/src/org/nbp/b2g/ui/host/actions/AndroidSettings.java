package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class AndroidSettings extends SystemActivityAction {
  @Override
  protected String getIntentAction () {
    return android.provider.Settings.ACTION_SETTINGS;
  }

  public AndroidSettings (Endpoint endpoint) {
    super(endpoint, false);
  }
}

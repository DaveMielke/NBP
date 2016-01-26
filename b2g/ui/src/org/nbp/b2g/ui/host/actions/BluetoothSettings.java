package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class BluetoothSettings extends SystemActivityAction {
  @Override
  protected String getIntentAction () {
    return android.provider.Settings.ACTION_BLUETOOTH_SETTINGS;
  }

  public BluetoothSettings (Endpoint endpoint) {
    super(endpoint, false);
  }
}

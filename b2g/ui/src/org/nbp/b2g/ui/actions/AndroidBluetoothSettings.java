package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;

public class AndroidBluetoothSettings extends ActivityAction {
  @Override
  protected Intent getIntent (Context context) {
    return new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
  }

  public AndroidBluetoothSettings () {
    super(false);
  }
}

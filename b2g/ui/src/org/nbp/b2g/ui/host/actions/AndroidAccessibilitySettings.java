package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;

public class AndroidAccessibilitySettings extends ActivityAction {
  @Override
  protected Intent getIntent (Context context) {
    return new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
  }

  public AndroidAccessibilitySettings (Endpoint endpoint) {
    super(endpoint, false);
  }
}

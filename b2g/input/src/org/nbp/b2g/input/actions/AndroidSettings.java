package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.content.Context;
import android.content.Intent;

public class AndroidSettings extends ActivityAction {
  @Override
  protected Intent getIntent (Context context) {
    return new Intent(android.provider.Settings.ACTION_SETTINGS);
  }

  public AndroidSettings () {
    super();
  }
}

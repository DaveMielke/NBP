package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;

public class Clock extends ActivityAction {
  @Override
  protected Intent getIntent (Context context) {
    return new Intent(context, ClockActivity.class);
  }

  public Clock (Endpoint endpoint) {
    super(endpoint, false);
  }
}

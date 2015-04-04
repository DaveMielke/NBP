package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;

public class Clock extends ActivityAction {
  @Override
  protected Intent getIntent (Context context) {
    return new Intent(context, ClockActivity.class);
  }

  public Clock () {
    super(false);
  }
}

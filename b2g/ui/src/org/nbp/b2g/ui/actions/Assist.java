package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;

public class Assist extends ActivityAction {
  @Override
  protected Intent getIntent (Context context) {
    return new Intent(Intent.ACTION_ASSIST);
  }

  public Assist () {
    super(false);
  }
}

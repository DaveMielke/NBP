package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Intent;

public class Messaging extends MainActivityAction {
  @Override
  protected String getCategory () {
    return Intent.CATEGORY_APP_MESSAGING;
  }

  public Messaging (Endpoint endpoint) {
    super(endpoint, false);
  }
}

package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Intent;

public class Listen extends SystemActivityAction {
  @Override
  protected String getIntentAction () {
    return Intent.ACTION_VOICE_COMMAND;
  }

  public Listen (Endpoint endpoint) {
    super(endpoint, false);
  }
}

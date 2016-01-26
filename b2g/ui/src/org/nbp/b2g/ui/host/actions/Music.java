package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Intent;

public class Music extends MainActivityAction {
  @Override
  protected String getCategory () {
    return Intent.CATEGORY_APP_MUSIC;
  }

  public Music (Endpoint endpoint) {
    super(endpoint, false);
  }
}

package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Intent;

public class People extends MainActivityAction {
  @Override
  protected String getCategory () {
    return Intent.CATEGORY_APP_CONTACTS;
  }

  public People (Endpoint endpoint) {
    super(endpoint, false);
  }
}

package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Intent;

public class Store extends MainActivityAction {
  @Override
  protected String getCategory () {
    return Intent.CATEGORY_APP_MARKET;
  }

  public Store (Endpoint endpoint) {
    super(endpoint, false);
  }
}

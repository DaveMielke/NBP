package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class Dialer extends SystemActivityAction {
  @Override
  protected String getIntentAction () {
    return android.content.Intent.ACTION_DIAL;
  }

  public Dialer (Endpoint endpoint) {
    super(endpoint, false);
  }
}

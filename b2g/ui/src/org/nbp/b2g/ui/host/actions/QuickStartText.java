package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class QuickStartText extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return QuickStartActivity.class;
  }

  public QuickStartText (Endpoint endpoint) {
    super(endpoint, false);
  }
}

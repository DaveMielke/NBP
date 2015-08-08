package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class Guide extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return GuideActivity.class;
  }

  public Guide (Endpoint endpoint) {
    super(endpoint, false);
  }
}

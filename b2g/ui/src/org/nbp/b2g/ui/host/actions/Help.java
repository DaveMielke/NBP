package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class Help extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return HelpActivity.class;
  }

  public Help (Endpoint endpoint) {
    super(endpoint, false);
  }
}

package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DescribeBuild extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return BuildActivity.class;
  }

  public DescribeBuild (Endpoint endpoint) {
    super(endpoint, false);
  }
}

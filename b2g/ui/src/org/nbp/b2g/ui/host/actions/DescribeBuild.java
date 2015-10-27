package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DescribeBuild extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return BuildDetailsActivity.class;
  }

  public DescribeBuild (Endpoint endpoint) {
    super(endpoint, false);
  }
}

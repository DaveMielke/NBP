package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class WarrantyText extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return WarrantyActivity.class;
  }

  public WarrantyText (Endpoint endpoint) {
    super(endpoint, false);
  }
}

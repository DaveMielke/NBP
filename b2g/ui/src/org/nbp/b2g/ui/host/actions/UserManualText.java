package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class UserManualText extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return UserManualActivity.class;
  }

  public UserManualText (Endpoint endpoint) {
    super(endpoint, false);
  }
}

package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class ShowNotificationsOn extends NextValueAction {
  public ShowNotificationsOn (Endpoint endpoint) {
    super(endpoint, Controls.showNotifications, false);
  }
}

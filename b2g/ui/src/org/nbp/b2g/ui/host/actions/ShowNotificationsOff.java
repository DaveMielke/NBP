package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class ShowNotificationsOff extends PreviousValueAction {
  public ShowNotificationsOff (Endpoint endpoint) {
    super(endpoint, Controls.showNotifications, false);
  }
}

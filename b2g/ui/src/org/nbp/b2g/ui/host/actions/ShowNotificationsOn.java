package org.nbp.b2g.ui.hosgt.actions;
import org.nbp.b2g.ui.hosgt.*;
import org.nbp.b2g.ui.*;

public class ShowNotificationsOn extends NextValueAction {
  public ShowNotificationsOn (Endpoint endpoint) {
    super(endpoint, Controls.getShowNotificationsControl(), false);
  }
}

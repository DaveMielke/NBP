package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;

public class RecentApplications extends GlobalAction {
  @Override
  protected int getGlobalAction () {
    return AccessibilityService.GLOBAL_ACTION_RECENTS;
  }

  public RecentApplications (Endpoint endpoint) {
    super(endpoint, false);
  }
}

package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.accessibilityservice.AccessibilityService;

public class RecentApps extends GlobalAction {
  @Override
  protected int getGlobalAction () {
    return AccessibilityService.GLOBAL_ACTION_RECENTS;
  }

  public RecentApps () {
    super();
  }
}

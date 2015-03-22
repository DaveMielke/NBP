package org.nbp.b2g.input;

import android.accessibilityservice.AccessibilityService;

public class RecentAppsAction extends GlobalAction {
  public RecentAppsAction () {
    super(AccessibilityService.GLOBAL_ACTION_RECENTS, "RECENT_APPS");
  }
}

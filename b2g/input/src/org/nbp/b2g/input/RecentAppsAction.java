package org.nbp.b2g.input;

import android.accessibilityservice.AccessibilityService;

public class RecentAppsAction extends GlobalAction {
  @Override
  protected int getGlobalAction () {
    return AccessibilityService.GLOBAL_ACTION_RECENTS;
  }

  public RecentAppsAction () {
    super();
  }
}

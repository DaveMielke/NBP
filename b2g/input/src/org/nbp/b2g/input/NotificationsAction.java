package org.nbp.b2g.input;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class NotificationsAction extends GlobalAction {
  public NotificationsAction () {
    super(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS, "NOTIFICATIONS", KeyEvent.KEYCODE_NOTIFICATION);
  }
}

package org.nbp.b2g.input;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class NotificationsAction extends GlobalAction {
  @Override
  protected int getGlobalAction () {
    return AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS;
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_NOTIFICATION;
  }

  public NotificationsAction () {
    super();
  }
}

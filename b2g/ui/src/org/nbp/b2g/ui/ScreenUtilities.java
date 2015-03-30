package org.nbp.b2g.ui;

import android.os.Build;

import android.view.accessibility.AccessibilityNodeInfo;

public class ScreenUtilities {
  public static boolean canAssign (Class to, AccessibilityNodeInfo from) {
    return LanguageUtilities.canAssign(to, from.getClassName());
  }

  public static boolean isEditable (AccessibilityNodeInfo node) {
    if (node == null) return false;

    if (ApplicationUtilities.haveSdkVersion(Build.VERSION_CODES.JELLY_BEAN_MR2)) {
      return node.isEditable();
    }

    return canAssign(android.widget.EditText.class, node);
  }

  private ScreenUtilities () {
  }
}

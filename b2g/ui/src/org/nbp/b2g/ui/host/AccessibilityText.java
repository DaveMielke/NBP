package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.HashMap;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class AccessibilityText {
  private final static Map<AccessibilityNodeInfo, CharSequence> accessibilityText
             = new HashMap<AccessibilityNodeInfo, CharSequence>();

  public static void set (AccessibilityNodeInfo node, CharSequence text) {
    synchronized (accessibilityText) {
      if (text != null) {
        accessibilityText.put(AccessibilityNodeInfo.obtain(node), text);
      } else {
        accessibilityText.remove(node);
      }
    }
  }

  public static CharSequence get (AccessibilityNodeInfo node) {
    synchronized (accessibilityText) {
      return accessibilityText.get(node);
    }
  }

  private AccessibilityText () {
  }
}

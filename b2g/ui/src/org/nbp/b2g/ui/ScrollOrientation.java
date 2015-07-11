package org.nbp.b2g.ui;

import android.view.accessibility.AccessibilityNodeInfo;

enum ScrollOrientation {
  VERTICAL(
    new Class[] {
      android.widget.ListView.class,
      android.widget.ScrollView.class
    }
  ),

  HORIZONTAL(
    new Class[] {
      android.widget.HorizontalScrollView.class
    },

    new String[] {
      "android.support.v4.view.ViewPager"
    }
  );

  private final Class[] classes;
  private final String[] names;

  public boolean isEligible (CharSequence className) {
    if (classes != null) {
      for (Class c : classes) {
        if (LanguageUtilities.canAssign(c, className)) {
          return true;
        }
      }
    }

    if (names != null) {
      for (String name : names) {
        if (name.equals(className)) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean isEligible (AccessibilityNodeInfo node) {
    return isEligible(node.getClassName());
  }

  public static ScrollOrientation getScrollOrientation (AccessibilityNodeInfo node) {
    for (ScrollOrientation orientation : ScrollOrientation.values()) {
      if (orientation.isEligible(node)) {
        return orientation;
      }
    }

    return null;
  }

  ScrollOrientation (Class[] classes, String[] names) {
    this.classes = classes;
    this.names = names;
  }

  ScrollOrientation (Class[] classes) {
    this(classes, null);
  }

  ScrollOrientation () {
    this(null);
  }
}

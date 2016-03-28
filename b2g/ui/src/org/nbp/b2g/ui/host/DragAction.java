package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Rect;
import android.graphics.Point;

public abstract class DragAction extends Action {
  private static Rect fromRegion = null;

  private final static AccessibilityNodeInfo getNode () {
    return Endpoints.host.get().getCurrentNode();
  }

  private final static Rect getRegion (AccessibilityNodeInfo node) {
    Rect region = new Rect();
    node.getBoundsInScreen(region);
    return region;
  }

  protected final static Rect getRegion () {
    AccessibilityNodeInfo node = getNode();
    if (node == null) return null;

    Rect region = getRegion(node);
    node.recycle();

    return region;
  }

  private final static Point getCenter (Rect region) {
    return new Point(
      ((region.left + region.right) / 2),
      ((region.top + region.bottom) / 2)
    );
  }

  protected final static boolean setFromRegion () {
    fromRegion = null;

    Rect region = getRegion();
    if (region == null) return false;

    fromRegion = region;
    return true;
  }

  protected final static Rect getFromRegion () {
    return fromRegion;
  }

  protected final static boolean haveFromRegion () {
    return fromRegion != null;
  }

  private final static boolean dropAt (Point location) {
    return false;
  }

  protected final static boolean dropAt (Rect region) {
    if (region == null) return false;
    return dropAt(getCenter(region));
  }

  protected final static boolean dropLeft (Rect region) {
    if (region == null) return false;
    Point location = getCenter(region);

    location.x = region.left - 1;
    return dropAt(location);
  }

  protected final static boolean dropRight (Rect region) {
    if (region == null) return false;
    Point location = getCenter(region);

    location.x = region.right;
    return dropAt(location);
  }

  protected final static boolean dropAbove (Rect region) {
    if (region == null) return false;
    Point location = getCenter(region);

    location.y = region.top - 1;
    return dropAt(location);
  }

  protected final static boolean dropBelow (Rect region) {
    if (region == null) return false;
    Point location = getCenter(region);

    location.y = region.bottom;
    return dropAt(location);
  }

  protected DragAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}

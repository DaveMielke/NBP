package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveLeft extends MoveBackward {
  @Override
  public boolean performAction () {
    if (BrailleDevice.moveLeft()) return true;
    return super.performAction();
  }

  public MoveLeft () {
    super();
  }
}

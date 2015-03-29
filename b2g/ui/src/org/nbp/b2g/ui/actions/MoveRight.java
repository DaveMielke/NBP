package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveRight extends MoveForward {
  @Override
  public boolean performAction () {
    if (BrailleDevice.moveRight()) return true;
    return super.performAction();
  }

  public MoveRight () {
    super();
  }
}

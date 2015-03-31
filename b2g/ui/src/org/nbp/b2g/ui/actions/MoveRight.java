package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class MoveRight extends MoveForward {
  @Override
  public boolean performAction () {
    if (BrailleDevice.panRight()) return true;
    if (ScreenUtilities.isEditable()) return false;
    return super.performAction();
  }

  public MoveRight () {
    super();
  }
}

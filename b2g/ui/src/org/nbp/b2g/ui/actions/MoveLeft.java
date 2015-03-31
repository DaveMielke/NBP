package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class MoveLeft extends MoveBackward {
  @Override
  public boolean performAction () {
    if (BrailleDevice.panLeft()) return true;
    if (ScreenUtilities.isEditable()) return false;
    return super.performAction();
  }

  public MoveLeft () {
    super();
  }
}

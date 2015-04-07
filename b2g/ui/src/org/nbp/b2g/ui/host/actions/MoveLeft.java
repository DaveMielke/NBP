package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class MoveLeft extends MoveBackward {
  private boolean panLeft () {
    int indent = BrailleDevice.getLineIndent();

    if (indent == 0) {
      int start = BrailleDevice.getLineStart();
      if (start == 0) return false;

      BrailleDevice.setLine(start-1);
      indent = BrailleDevice.getLineLength() + 1;
    } else {
      int length = BrailleDevice.getLineLength();
      if (indent > length) indent = length;
    }

    if ((indent -= BrailleDevice.getBrailleLength()) < 0) indent = 0;
    BrailleDevice.setLineIndent(indent);
    return BrailleDevice.write();
  }

  @Override
  public boolean performAction () {
    synchronized (BrailleDevice.LOCK) {
      if (panLeft()) return true;
      if (ScreenUtilities.isEditable()) return false;
    }

    return super.performAction();
  }

  public MoveLeft (Endpoint endpoint) {
    super(endpoint);
  }
}

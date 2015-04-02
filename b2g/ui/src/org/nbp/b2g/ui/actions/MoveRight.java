package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class MoveRight extends MoveForward {
  private boolean panRight () {
    int indent = BrailleDevice.getLineIndent() + BrailleDevice.getBrailleLength();
    int length = BrailleDevice.getLineLength();

    if (indent > length) {
      int offset = BrailleDevice.getLineStart() + length + 1;
      if (offset > BrailleDevice.getTextLength()) return false;

      BrailleDevice.setLine(offset);
      indent = 0;
    }

    BrailleDevice.setLineIndent(indent);
    return BrailleDevice.write();
  }

  @Override
  public boolean performAction () {
    synchronized (BrailleDevice.LOCK) {
      if (panRight()) return true;
      if (ScreenUtilities.isEditable()) return false;
    }

    return super.performAction();
  }

  public MoveRight () {
    super();
  }
}

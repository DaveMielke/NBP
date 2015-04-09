package org.nbp.b2g.ui.bluetooth;
import org.nbp.b2g.ui.*;

public class BaumBrailleDisplay extends BrailleDisplay {
  @Override
  protected void resetInput (boolean readTimedOut) {
  }

  @Override
  protected boolean handleInput (int b) {
    logIgnoredByte(b);
    return true;
  }

  public BaumBrailleDisplay () {
    super();
  }
}

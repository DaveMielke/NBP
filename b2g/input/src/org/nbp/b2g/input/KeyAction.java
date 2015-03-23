package org.nbp.b2g.input;

public abstract class KeyAction extends Action {
  protected long getHoldTime () {
    return 0;
  }

  protected void waitForHoldTime () {
    long holdTime = getHoldTime();

    if (holdTime > 0) ApplicationUtilities.sleep(holdTime + ApplicationParameters.LONG_PRESS_DELAY);
  }

  protected KeyAction () {
    super();
  }
}

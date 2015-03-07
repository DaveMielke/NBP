package org.nbp.b2g.input;

public class PowerKey extends UInputDevice {
  private native int getKey ();
  private final int powerKey = getKey();

  @Override
  protected boolean prepareDevice (int device) {
    if (enableKeyEvents(device)) {
      if (enableKey(device, powerKey)) {
        return true;
      }
    }

    return false;
  }

  public boolean press () {
    return pressKey(powerKey);
  }

  public boolean release () {
    return releaseKey(powerKey);
  }

  public PowerKey () {
    super();
  }
}

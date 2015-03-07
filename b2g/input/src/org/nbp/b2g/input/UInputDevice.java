package org.nbp.b2g.input;

class UInputDevice {
  private final static int NOT_OPEN = -1;
  private int device = NOT_OPEN;

  private native int openDevice ();
  private native void closeDevice (int device);

  private native boolean pressKey (int device);
  private native boolean releaseKey (int device);

  protected boolean open () {
    if (device == NOT_OPEN) {
      if ((device = openDevice()) == NOT_OPEN) {
        return false;
      }
    }

    return true;
  }

  public boolean press () {
    if (open()) {
      if (pressKey(device)) {
        return true;
      }
    }

    return false;
  }

  public boolean release () {
    if (open()) {
      if (releaseKey(device)) {
        return true;
      }
    }

    return false;
  }

  public UInputDevice () {
  }
}

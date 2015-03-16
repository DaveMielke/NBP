package org.nbp.b2g.input;

public abstract class UInputDevice {
  protected abstract boolean prepareDevice (int device);

  private final static int NOT_OPEN = -1;
  private int uinputDevice = NOT_OPEN;

  private native int openDevice ();
  private native boolean createDevice (int device);
  private native void closeDevice (int device);

  protected native boolean enableKeyEvents (int device);
  protected native boolean enableKey (int device, int key);
  private native boolean pressKey (int device, int key);
  private native boolean releaseKey (int device, int key);

  protected boolean open () {
    if (uinputDevice != NOT_OPEN) return true;

    int device;
    if ((device = openDevice()) != NOT_OPEN) {
      if (prepareDevice(device)) {
        if (createDevice(device)) {
          uinputDevice = device;
          return true;
        }
      }

      closeDevice(device);
    }

    return false;
  }

  public void close () {
    if (uinputDevice != NOT_OPEN) {
      int device = uinputDevice;
      uinputDevice = NOT_OPEN;
      closeDevice(device);
    }
  }

  public boolean pressKey (int key) {
    if (open()) {
      if (pressKey(uinputDevice, key)) {
        return true;
      }
    }

    return false;
  }

  public boolean releaseKey (int key) {
    if (open()) {
      if (releaseKey(uinputDevice, key)) {
        return true;
      }
    }

    return false;
  }

  public UInputDevice () {
  }

  static {
    System.loadLibrary("InputService");
  }
}

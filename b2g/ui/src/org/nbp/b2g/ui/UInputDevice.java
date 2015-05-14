package org.nbp.b2g.ui;

import android.graphics.Point;

public abstract class UInputDevice {
  protected abstract boolean prepareDevice (int device);

  private final static int NOT_OPEN = -1;
  private int uinputDevice = NOT_OPEN;

  private native int openDevice (int width, int height);
  private native boolean createDevice (int device);
  private native void closeDevice (int device);

  protected native boolean enableKeyEvents (int device);
  protected native boolean enableKey (int device, int key);
  private native boolean pressKey (int device, int key);
  private native boolean releaseKey (int device, int key);

  protected native boolean enableTouchEvents (int device);
  protected native boolean tap (int device, int x, int y);
  protected native boolean swipe (int device, int x1, int y1, int x2, int y2);

  protected boolean open () {
    if (uinputDevice != NOT_OPEN) return true;

    int device;
    {
      Point size = ApplicationContext.getScreenSize();
      int width;
      int height;

      if (size != null) {
        width = size.x;
        height = size.y;
      } else {
        width = 0;
        height = 0;
      }

      device = openDevice(width, height);
    }

    if (device != NOT_OPEN) {
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

  public boolean sendKeyPress (int key) {
    if (open()) {
      if (pressKey(uinputDevice, key)) {
        return true;
      }
    }

    return false;
  }

  public boolean sendKeyRelease (int key) {
    if (open()) {
      if (releaseKey(uinputDevice, key)) {
        return true;
      }
    }

    return false;
  }

  public boolean sendKeyEvent (int key, boolean press) {
    return press? sendKeyPress(key): sendKeyRelease(key);
  }

  public boolean sendTap (int x, int y) {
    if (open()) {
      if (tap(uinputDevice, x, y)) {
        return true;
      }
    }

    return false;
  }

  public boolean sendSwipe (int x1, int y1, int x2, int y2) {
    if (open()) {
      if (swipe(uinputDevice, x1, y1, x2, y2)) {
        return true;
      }
    }

    return false;
  }

  protected UInputDevice () {
  }

  static {
    System.loadLibrary("UserInterface");
  }
}

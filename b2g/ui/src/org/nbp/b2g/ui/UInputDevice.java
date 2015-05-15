package org.nbp.b2g.ui;

import android.util.Log;

import android.graphics.Point;

public abstract class UInputDevice {
  private final static String LOG_TAG = UInputDevice.class.getName();

  protected abstract boolean prepareDevice (int device);

  private final static int NOT_OPEN = -1;
  private int uinputDevice = NOT_OPEN;

  private native int openDevice (int width, int height);
  private native boolean createDevice (int device);
  private native void closeDevice (int device);

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

  protected int getDevice () {
    return uinputDevice;
  }

  protected UInputDevice () {
  }

  static {
    System.loadLibrary("UserInterface");
  }
}

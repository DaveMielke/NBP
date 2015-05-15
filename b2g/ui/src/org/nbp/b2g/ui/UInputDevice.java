package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.util.Log;

public abstract class UInputDevice {
  private final static String LOG_TAG = UInputDevice.class.getName();

  protected abstract boolean prepareDevice (ByteBuffer device);

  private ByteBuffer uinputDevice = null;

  private native ByteBuffer openDevice (String name);
  private native boolean createDevice (ByteBuffer device);
  private native void closeDevice (ByteBuffer device);

  protected boolean open () {
    if (uinputDevice != null) return true;

    ByteBuffer device = openDevice(getClass().getName());
    if (device != null) {
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
    if (uinputDevice != null) {
      ByteBuffer device = uinputDevice;
      uinputDevice = null;
      closeDevice(device);
    }
  }

  protected ByteBuffer getDevice () {
    return uinputDevice;
  }

  protected UInputDevice () {
  }

  static {
    System.loadLibrary("UserInterface");
  }
}

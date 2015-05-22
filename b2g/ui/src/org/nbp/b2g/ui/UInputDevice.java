package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.util.Log;

public abstract class UInputDevice {
  private final static String LOG_TAG = UInputDevice.class.getName();

  protected abstract boolean prepareDevice (ByteBuffer uinput);

  private ByteBuffer uinputDescriptor = null;

  private native ByteBuffer openDevice (String name);
  private native boolean createDevice (ByteBuffer uinput);
  private native void closeDevice (ByteBuffer uinput);

  protected boolean open () {
    if (uinputDescriptor != null) return true;

    ByteBuffer uinput = openDevice(getClass().getName());
    if (uinput != null) {
      if (prepareDevice(uinput)) {
        if (createDevice(uinput)) {
          uinputDescriptor = uinput;
          return true;
        }
      }

      closeDevice(uinput);
    }

    return false;
  }

  public void close () {
    if (uinputDescriptor != null) {
      ByteBuffer uinput = uinputDescriptor;
      uinputDescriptor = null;
      closeDevice(uinput);
    }
  }

  protected ByteBuffer getUInputDescriptor () {
    return open()? uinputDescriptor: null;
  }

  protected UInputDevice () {
  }

  static {
    System.loadLibrary("UserInterface");
  }
}

package org.nbp.b2g.ui;

public abstract class Devices {
  private final static Object keyboardLock = new Object();
  private static KeyboardDevice keyboardDevice = null;

  public static KeyboardDevice getKeyboardDevice () {
    synchronized (keyboardLock) {
      if (keyboardDevice == null) keyboardDevice = new KeyboardDevice();
      return keyboardDevice;
    }
  }

  private final static Object touchLock = new Object();
  private static TouchDevice touchDevice = null;

  public static TouchDevice getTouchDevice () {
    synchronized (touchLock) {
      if (touchDevice == null) touchDevice = new TouchDevice();
      return touchDevice;
    }
  }

  private final static Object motionLock = new Object();
  private static MotionDevice motionDevice = null;

  public static MotionDevice getMotionDevice () {
    synchronized (motionLock) {
      if (motionDevice == null) motionDevice = new MotionDevice();
      return motionDevice;
    }
  }

  private final static Object speechLock = new Object();
  private static SpeechDevice speechDevice = null;

  public static SpeechDevice getSpeechDevice () {
    synchronized (speechLock) {
      if (speechDevice == null) speechDevice = new SpeechDevice();
      return speechDevice;
    }
  }

  private Devices () {
  }
}

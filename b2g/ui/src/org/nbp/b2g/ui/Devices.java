package org.nbp.b2g.ui;

public class Devices {
  private final static KeyboardDevice keyboardDevice = new KeyboardDevice();
  private final static TouchDevice touchDevice = new TouchDevice();
  private final static SpeechDevice speechDevice = new SpeechDevice();

  public static KeyboardDevice getKeyboardDevice () {
    return keyboardDevice;
  }

  public static TouchDevice getTouchDevice () {
    return touchDevice;
  }

  public static SpeechDevice getSpeechDevice () {
    return speechDevice;
  }

  private Devices () {
  }
}

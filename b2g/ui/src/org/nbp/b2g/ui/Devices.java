package org.nbp.b2g.ui;

public class Devices {
  private final static KeyboardDevice keyboardDevice = new KeyboardDevice();
  private final static SpeechDevice speechDevice = new SpeechDevice();

  public static KeyboardDevice getKeyboardDevice () {
    return keyboardDevice;
  }

  public static SpeechDevice getSpeechDevice () {
    return speechDevice;
  }

  private Devices () {
  }
}

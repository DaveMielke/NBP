package org.nbp.b2g.ui;

public class Devices {
  private final static KeyboardDevice keyboardDevice = new KeyboardDevice();

  public static KeyboardDevice getKeyboardDevice () {
    return keyboardDevice;
  }

  private Devices () {
  }
}

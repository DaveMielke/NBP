package org.liblouis;

public abstract class Emphasis {
  public native static int getCount ();
  public native static short getBit (int number);

  public native static short getBoldBit ();
  public native static short getItalicBit ();
  public native static short getUnderlineBit ();

  private Emphasis () {
  }
}

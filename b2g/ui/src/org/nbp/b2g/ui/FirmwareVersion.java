package org.nbp.b2g.ui;

public abstract class FirmwareVersion {
  public static native int getMainMajor ();
  public static native int getMainMinor ();

  public static native int getBaseMajor ();
  public static native int getBaseMinor ();

  private FirmwareVersion () {
  }

  static {
    ApplicationUtilities.loadLibrary();
  }
}

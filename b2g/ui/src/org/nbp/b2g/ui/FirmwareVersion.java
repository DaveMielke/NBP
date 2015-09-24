package org.nbp.b2g.ui;

public abstract class FirmwareVersion {
  public static native int getMajor ();
  public static native int getMinor ();

  private FirmwareVersion () {
  }

  static {
    ApplicationUtilities.loadLibrary();
  }
}

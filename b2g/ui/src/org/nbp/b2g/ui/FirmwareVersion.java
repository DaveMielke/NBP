package org.nbp.b2g.ui;

public abstract class FirmwareVersion {
  public static native byte getMajor ();
  public static native byte getMinor ();

  private FirmwareVersion () {
  }

  static {
    ApplicationUtilities.loadLibrary();
  }
}

package org.nbp.b2g.ui;

public class MetecBrailleDevice extends BrailleDevice {
  @Override
  protected native boolean connectDevice ();

  @Override
  protected native void disconnectDevice ();

  @Override
  public native String getDriverVersion ();

  @Override
  protected native int getCellCount ();

  @Override
  protected native boolean setCellFirmness (int firmness);

  @Override
  protected native boolean clearCells ();

  @Override
  protected native boolean writeCells (byte[] cells);

  private native boolean enableDevice ();
  private native boolean disableDevice ();

  private static boolean canEnableDisable () {
    return FirmwareVersion.getMainMajor() >= 3;
  }

  @Override
  public final boolean enable () {
    if (!canEnableDisable()) return true;

    if (isConnected()) {
      if (enableDevice()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public final boolean disable () {
    if (!canEnableDisable()) return true;

    if (isConnected()) {
      if (disableDevice()) {
        return true;
      }
    }

    return false;
  }

  public MetecBrailleDevice () {
  }

  static {
    ApplicationUtilities.loadLibrary();
  }
}

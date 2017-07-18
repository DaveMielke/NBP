package org.nbp.b2g.ui;

public class MetecBrailleDevice extends BrailleDevice {
  private final native boolean enableDevice ();
  private final native boolean disableDevice ();

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

  private final native int getMaximumFirmness ();
  private final native boolean setFirmness (int firmness);

  @Override
  public final boolean setFirmness (GenericLevel firmness) {
    synchronized (this) {
      if (isConnected()) {
        if (setFirmness(firmness.getValue(0, getMaximumFirmness()))) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  protected final native boolean connectDevice ();

  @Override
  protected final native void disconnectDevice ();

  @Override
  public final native String getDriverVersion ();

  @Override
  protected final native int getCellCount ();

  @Override
  protected final native boolean clearCells ();

  @Override
  protected final native boolean writeCells (byte[] cells);

  public MetecBrailleDevice () {
  }

  static {
    ApplicationUtilities.loadLibrary();
  }
}

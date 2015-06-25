package org.nbp.b2g.ui;

import java.util.Arrays;

import android.util.Log;

public class BrailleDevice {
  private final static String LOG_TAG = BrailleDevice.class.getName();

  public final static byte DOT_1 =       0X01;
  public final static byte DOT_2 =       0X02;
  public final static byte DOT_3 =       0X04;
  public final static byte DOT_4 =       0X08;
  public final static byte DOT_5 =       0X10;
  public final static byte DOT_6 =       0X20;
  public final static byte DOT_7 =       0X40;
  public final static byte DOT_8 = (byte)0X80;

  private native boolean openDevice ();
  private native void closeDevice ();

  private native String getVersion ();
  private native int getCellCount ();

  private native boolean clearCells ();
  private native boolean writeCells (byte[] cells);

  private byte[] brailleCells = null;
  private boolean writePending = false;

  private void logCells (String action) {
    if (ApplicationSettings.LOG_UPDATES) {
      Log.v(LOG_TAG, String.format(
        "braille cells: %s: %s",
        action, Braille.toString(brailleCells)
      ));
    }
  }

  public byte[] getCells () {
    synchronized (this) {
      if (!open()) return null;
      return Arrays.copyOf(brailleCells, brailleCells.length);
    }
  }

  public boolean open () {
    synchronized (this) {
      if (brailleCells != null) return true;

      if (openDevice()) {
        int cellCount = getCellCount();

        if (cellCount > 0) {
          brailleCells = new byte[cellCount];
          Log.d(LOG_TAG, "braille cell count: " + brailleCells.length);

          String version = getVersion();
          Log.d(LOG_TAG, "braille device version: " + version);

          clearCells();
          Braille.clearCells(brailleCells);
          writePending = false;

          return true;
        }

        closeDevice();
      }
    }

    return false;
  }

  public void close () {
    synchronized (this) {
      if (brailleCells != null) {
        brailleCells = null;
        closeDevice();
      }
    }
  }

  public int size () {
    synchronized (this) {
      if (open()) return brailleCells.length;
      return 0;
    }
  }

  private boolean writeCells () {
    logCells("writing");
    return writeCells(brailleCells);
  }

  private Timeout writeDelay = new Timeout(ApplicationParameters.BRAILLE_WRITE_DELAY, "braille-device-write-delay") {
    @Override
    public void run () {
      synchronized (BrailleDevice.this) {
        if (writePending) {
          if (writeCells()) writePending = false;
          start(ApplicationParameters.BRAILLE_REWRITE_DELAY);
        }
      }
    }
  };

  public boolean write () {
    synchronized (this) {
      if (!open()) return false;

      {
        byte[] oldCells = getCells();
        Braille.setCells(brailleCells);
        if (Arrays.equals(brailleCells, oldCells)) return true;
      }

      writePending = true;
      logCells("updated");
    }

    synchronized (writeDelay) {
      if (!writeDelay.isActive()) {
        writeDelay.start();
      }
    }

    return true;
  }

  public boolean write (Endpoint endpoint, String message) {
    synchronized (this) {
      if (open()) {
        writeDelay.cancel();

        byte[] cells = new byte[brailleCells.length];
        Braille.setCells(cells, message, endpoint.getCharacters());

        if (writeCells(cells)) {
          writePending = true;
          writeDelay.start(ApplicationParameters.BRAILLE_MESSAGE_TIME);
          return true;
        }
      }
    }

    return false;
  }

  public BrailleDevice () {
  }

  static {
    System.loadLibrary("UserInterface");
  }
}

package org.nbp.b2g.ui;

import java.util.Arrays;

import android.util.Log;
import android.content.Context;

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

  private BrailleWindow brailleWindow = null;

  private BrailleWindow getWindow () {
    synchronized (this) {
      if (brailleWindow == null) {
        Context context = ApplicationContext.getContext();
        if (context == null) return null;
        brailleWindow = new BrailleWindow(context);
        brailleWindow.showWindow();
      }
    }

    return brailleWindow;
  }

  private native boolean openDevice ();
  private native void closeDevice ();

  private native boolean enableDevice ();
  private native boolean disableDevice ();

  private native String getDriverVersion ();
  private native int getCellCount ();

  private native boolean setCellFirmness (int firmness);

  private native boolean clearCells ();
  private native boolean writeCells (byte[] cells);

  private byte[] brailleCells = null;
  private String brailleText = null;
  private boolean writePending = false;

  private void logCells (byte[] cells, String reason) {
    if (ApplicationSettings.LOG_BRAILLE) {
      Log.v(LOG_TAG, String.format(
        "braille cells: %s: %s",
        reason, Braille.toString(cells)
      ));
    }
  }

  public byte[] getCells () {
    synchronized (this) {
      if (!open()) return null;
      return Arrays.copyOf(brailleCells, brailleCells.length);
    }
  }

  public void restoreControls () {
    Control[] controls = new Control[] {
      Controls.getBrailleFirmnessControl()
    };

    Controls.forEachControl(controls, Controls.restoreCurrentValue);
  }

  public boolean open () {
    synchronized (this) {
      if (brailleCells != null) return true;

      if (openDevice()) {
        int cellCount = getCellCount();

        if (cellCount > 0) {
          brailleCells = new byte[cellCount];
          Log.d(LOG_TAG, "braille cell count: " + brailleCells.length);

          String version = getDriverVersion();
          Log.d(LOG_TAG, "braille driver version: " + version);

          clearCells();
          Braille.clearCells(brailleCells);
          brailleText = "";
          writePending = false;

          restoreControls();
          return true;
        }

        closeDevice();
      }
    }

    return false;
  }

  public void close () {
    synchronized (writeDelay) {
      writeDelay.cancel();

      synchronized (this) {
        writePending = false;
        brailleText = null;

        if (brailleCells != null) {
          brailleCells = null;
          closeDevice();
        }
      }
    }
  }

  public boolean enable () {
    if (open()) {
      if (enableDevice()) {
        return true;
      }
    }

    return false;
  }

  public boolean disable () {
    if (open()) {
      if (disableDevice()) {
        return true;
      }
    }

    return false;
  }

  public int getLength () {
    synchronized (this) {
      if (open()) return brailleCells.length;
      return 0;
    }
  }

  public boolean setFirmness (int firmness) {
    synchronized (this) {
      if (open()) {
        if (setCellFirmness(firmness)) {
          return true;
        }
      }
    }

    return false;
  }

  private void writeText (String text) {
    BrailleWindow window = getWindow();

    if (window != null) {
      window.setText(text);

      if (ApplicationSettings.LOG_BRAILLE) {
        Log.d(LOG_TAG, "braille text: " + text);
      }
    }
  }

  private boolean writeCells (byte[] cells, String reason) {
    final int suppliedLength = cells.length;
    final int requiredLength = brailleCells.length;

    if (suppliedLength != requiredLength) {
      byte[] newCells = new byte[requiredLength];
      final int count = Math.min(suppliedLength, requiredLength);

      System.arraycopy(cells, 0, newCells, 0, count);
      Braille.clearCells(newCells, count);

      cells = newCells;
    }

    logCells(cells, reason);
    return writeCells(cells);
  }

  private boolean writeCells () {
    return writeCells(brailleCells, "writing");
  }

  private final Timeout writeDelay = new Timeout(ApplicationParameters.BRAILLE_WRITE_DELAY, "braille-device-write-delay") {
    @Override
    public void run () {
      synchronized (BrailleDevice.this) {
        if (writePending) {
          if (writeCells()) {
            writeText(brailleText);
            writePending = false;
          }

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
        String text = Braille.setCells(brailleCells);

        if (!text.equals(brailleText)) {
          brailleText = text;
        } else if (Arrays.equals(brailleCells, oldCells)) {
          return true;
        }
      }

      writePending = true;
      logCells(brailleCells, "updated");
    }

    synchronized (writeDelay) {
      if (!writeDelay.isActive()) {
        writeDelay.start();
      }
    }

    return true;
  }

  public boolean write (byte[] cells, long duration) {
    synchronized (this) {
      if (open()) {
        writeDelay.cancel();

        if (writeCells(cells, "message")) {
          if (duration > 0) {
            writePending = true;
            writeDelay.start(duration);
          }

          return true;
        }
      }
    }

    return false;
  }

  public boolean write (byte[] cells) {
    return write(cells, 0);
  }

  public boolean write (String text, long duration) {
    byte[] cells = new byte[text.length()];
    text = Braille.setCells(cells, text);

    boolean written = write(cells, duration);
    if (written) writeText(text);
    return written;
  }

  public boolean write (String text) {
    return write(text, 0);
  }

  public BrailleDevice () {
  }

  static {
    System.loadLibrary("UserInterface");
  }
}

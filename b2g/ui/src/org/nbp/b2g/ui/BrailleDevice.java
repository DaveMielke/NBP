package org.nbp.b2g.ui;

import java.util.Arrays;

import org.nbp.common.Timeout;
import org.nbp.common.Control;

import android.util.Log;
import android.content.Context;

import org.liblouis.BrailleTranslation;

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

  private BrailleMonitorWindow monitorWindow = null;

  public final BrailleMonitorWindow getMonitorWindow () {
    synchronized (this) {
      if (monitorWindow == null) {
        Context context = ApplicationContext.getContext();
        if (context == null) return null;
        monitorWindow = new BrailleMonitorWindow(context);
      }
    }

    return monitorWindow;
  }

  private native boolean openDevice ();
  private native void closeDevice ();

  private native boolean enableDevice ();
  private native boolean disableDevice ();

  public native String getDriverVersion ();
  private native int getCellCount ();

  private native boolean setCellFirmness (int firmness);

  private native boolean clearCells ();
  private native boolean writeCells (byte[] cells);

  private byte[] brailleCells = null;
  private CharSequence brailleText = null;
  private boolean writePending = false;

  public final byte[] getCells () {
    synchronized (this) {
      if (!open()) return null;
      return Arrays.copyOf(brailleCells, brailleCells.length);
    }
  }

  public final void restoreControls () {
    Control[] controls = new Control[] {
      Controls.brailleEnabled,
      Controls.brailleFirmness
    };

    Controls.forEachControl(controls, Controls.restoreCurrentValue);
  }

  private final boolean isOpen () {
    return brailleCells != null;
  }

  public final boolean open () {
    synchronized (this) {
      if (isOpen()) return true;

      if (openDevice()) {
        int cellCount = getCellCount();

        if (cellCount > 0) {
          brailleCells = new byte[cellCount];
          Log.d(LOG_TAG, "braille cell count: " + brailleCells.length);

          String version = getDriverVersion();
          Log.d(LOG_TAG, "braille driver version: " + version);

          clearCells();
          BrailleUtilities.clearCells(brailleCells);
          brailleText = "";
          writePending = false;

          restoreControls();
          return writeCells();
        }

        closeDevice();
      }
    }

    return false;
  }

  public final void close () {
    synchronized (writeDelay) {
      writeDelay.cancel();

      synchronized (this) {
        writePending = false;
        brailleText = null;

        if (isOpen()) {
          brailleCells = null;
          closeDevice();
        }
      }
    }
  }

  public static boolean canEnableDisable () {
    return FirmwareVersion.getMainMajor() >= 3;
  }

  public final boolean enable () {
    if (!canEnableDisable()) return true;

    if (isOpen()) {
      if (enableDevice()) {
        return true;
      }
    }

    return false;
  }

  public final boolean disable () {
    if (!canEnableDisable()) return true;

    if (isOpen()) {
      if (disableDevice()) {
        return true;
      }
    }

    return false;
  }

  public final int getLength () {
    synchronized (this) {
      if (open()) return brailleCells.length;
      return 0;
    }
  }

  public final boolean setFirmness (int firmness) {
    synchronized (this) {
      if (isOpen()) {
        if (setCellFirmness(firmness)) {
          return true;
        }
      }
    }

    return false;
  }

  private final void logCells (byte[] cells, String reason, CharSequence text) {
    boolean log = ApplicationSettings.LOG_BRAILLE;
    String braille = BrailleUtilities.toString(cells);

    if (log) {
      Log.d(LOG_TAG, String.format(
        "braille cells: %s: %s", reason, braille
      ));
    }

    if (text != null) {
      BrailleMonitorWindow window = getMonitorWindow();
      if (window != null) window.setContent(braille, text);
      if (log) Log.d(LOG_TAG, "braille text: " + text);
    }
  }

  private final void logCells (byte[] cells, String reason) {
    logCells(cells, reason, null);
  }

  private final boolean writeCells (byte[] cells, CharSequence text, String reason) {
    {
      final int suppliedLength = cells.length;
      final int requiredLength = brailleCells.length;

      if (suppliedLength != requiredLength) {
        byte[] newCells = new byte[requiredLength];
        final int count = Math.min(suppliedLength, requiredLength);

        System.arraycopy(cells, 0, newCells, 0, count);
        BrailleUtilities.clearCells(newCells, count);

        cells = newCells;
      }
    }

    if (ApplicationSettings.BRAILLE_ENABLED) {
      if (!writeCells(cells)) {
        return false;
      }
    }

    logCells(cells, reason, text);
    return true;
  }

  private final boolean writeCells () {
    return writeCells(brailleCells, brailleText, "writing");
  }

  public final boolean refreshCells () {
    synchronized (this) {
      if (!isOpen()) return true;
      return writeCells();
    }
  }

  private final Timeout writeDelay = new Timeout(ApplicationParameters.BRAILLE_WRITE_DELAY, "braille-device-write-delay") {
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

  public final void dismiss () {
    synchronized (writeDelay) {
      if (writeDelay.isActive()) {
        writeDelay.cancel();
        writeDelay.start();
      }
    }
  }

  public final boolean write (Endpoint endpoint) {
    synchronized (endpoint) {
      synchronized (this) {
        if (!open()) return false;

        {
          byte[] oldCells = getCells();
          CharSequence text = BrailleUtilities.setCells(brailleCells, endpoint);

          if (!text.equals(brailleText)) {
            brailleText = text;
          } else if (Arrays.equals(brailleCells, oldCells)) {
            return true;
          }
        }

        writePending = true;
        logCells(brailleCells, "updated");
      }
    }

    synchronized (writeDelay) {
      if (!writeDelay.isActive()) {
        writeDelay.start();
      }
    }

    return true;
  }

  public final boolean write (byte[] cells, CharSequence text, long duration) {
    synchronized (this) {
      if (open()) {
        writeDelay.cancel();

        if (writeCells(cells, text, "message")) {
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

  public final boolean write (byte[] cells, CharSequence text) {
    return write(cells, text, 0);
  }

  public final boolean write (byte[] cells, long duration) {
    return write(cells, "", 0);
  }

  public final boolean write (byte[] cells) {
    return write(cells, 0);
  }

  public final boolean write (CharSequence text, long duration) {
    byte[] cells = new byte[getLength()];
    BrailleTranslation brl = TranslationUtilities.newBrailleTranslation(text, true);
    int textLength;

    if (brl != null) {
      CharSequence braille = brl.getBrailleAsString();
      int brailleLength = BrailleUtilities.setCells(cells, braille);
      textLength = brl.findFirstTextOffset(brl.getTextOffset(brailleLength));
    } else {
      textLength = BrailleUtilities.setCells(cells, text);
    }

    text = text.subSequence(0, textLength);
    return write(cells, text, duration);
  }

  public final boolean write (CharSequence text) {
    return write(text, 0);
  }

  public BrailleDevice () {
  }

  static {
    ApplicationUtilities.loadLibrary();
  }
}

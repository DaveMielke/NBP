package org.nbp.b2g.ui;

import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;

import org.nbp.common.Timeout;
import org.nbp.common.Control;

import android.util.Log;
import android.content.Context;

import org.liblouis.BrailleTranslation;

public abstract class BrailleDevice {
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

  protected abstract boolean connectDevice ();
  protected abstract void disconnectDevice ();

  public abstract String getDriverVersion ();
  protected abstract int getCellCount ();

  protected abstract boolean clearCells ();
  protected abstract boolean writeCells (byte[] cells);

  private class WriteElement {
    public final byte[] cells = new byte[getLength()];
    public final CharSequence text;

    public WriteElement (CharSequence text) {
      BrailleTranslation brl = TranslationUtilities.newBrailleTranslation(text, true);
      int textLength;

      if (brl != null) {
        CharSequence braille = brl.getBrailleAsString();
        int brailleLength = BrailleUtilities.setCells(cells, braille);
        textLength = brl.findFirstTextOffset(brl.getTextOffset(brailleLength));
      } else {
        textLength = BrailleUtilities.setCells(cells, text);
      }

      this.text = text.subSequence(0, textLength);
    }
  }

  private byte[] brailleCells = null;
  private CharSequence brailleText = null;
  private boolean writePending = false;

  private final Queue<WriteElement> messageQueue = new LinkedList<WriteElement>();
  private boolean messageActive = false;

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

  private final boolean writeCells (boolean immediate) {
    long delay = ApplicationParameters.BRAILLE_REWRITE_DELAY;

    if (immediate) {
      WriteElement message = messageQueue.poll();
      byte[] cells;
      CharSequence text;
      String reason;

      if (message != null) {
        messageActive = true;
        writePending = true;

        cells = message.cells;
        text = message.text;
        reason = "message";
        delay = ApplicationParameters.BRAILLE_MESSAGE_DURATION;
      } else {
        messageActive = false;
        if (!writePending) return true;
        writePending = false;

        cells = brailleCells;
        text = brailleText;
        reason = "writing";
      }

      if (!writeCells(cells, text, reason)) {
        writePending = true;
        return false;
      }
    } else if (writeDelay.isActive()) {
      return true;
    }

    writeDelay.start(delay);
    return true;
  }

  private final Timeout writeDelay =
    new Timeout(ApplicationParameters.BRAILLE_WRITE_DELAY, "braille-device-write-delay") {
      @Override
      public void run () {
        synchronized (BrailleDevice.this) {
          writeCells(true);
        }
      }
    };

  private final void restoreControls () {
    Control[] controls = new Control[] {
      Controls.brailleEnabled,
      Controls.brailleFirmness
    };

    Control.restoreCurrentValues(controls);
  }

  protected final boolean isConnected () {
    return brailleCells != null;
  }

  public final boolean connect () {
    synchronized (writeDelay) {
      synchronized (this) {
        if (isConnected()) return true;

        if (connectDevice()) {
          {
            String version = getDriverVersion();
            Log.i(LOG_TAG, "braille driver version: " + version);
          }

          brailleCells = new byte[getCellCount()];
          Log.i(LOG_TAG, "braille cell count: " + brailleCells.length);
          BrailleUtilities.clearCells(brailleCells);

          brailleText = "";
          writePending = false;

          messageQueue.clear();
          messageActive = false;

          if (clearCells()) {
            restoreControls();

            if (writeCells(false)) {
              return true;
            }
          }

          disconnectDevice();
        }
      }
    }

    return false;
  }

  public final void disconnect () {
    synchronized (writeDelay) {
      synchronized (this) {
        writeDelay.cancel();
        writePending = false;
        brailleText = null;

        if (isConnected()) {
          brailleCells = null;
          disconnectDevice();
        }
      }
    }
  }

  public boolean enable () {
    return true;
  }

  public boolean disable () {
    return false;
  }

  public boolean setCellFirmness (GenericLevel firmness) {
    return false;
  }

  public final int getLength () {
    synchronized (this) {
      if (connect()) return brailleCells.length;
      return 0;
    }
  }

  public final byte[] getCells () {
    synchronized (this) {
      if (!connect()) return null;
      return Arrays.copyOf(brailleCells, brailleCells.length);
    }
  }

  public final boolean refresh () {
    synchronized (writeDelay) {
      synchronized (this) {
        writePending = true;
        return writeCells(false);
      }
    }
  }

  private final boolean write (CharSequence text, byte[] cells) {
    synchronized (writeDelay) {
      synchronized (this) {
        if (!connect()) return false;

        if (!text.equals(brailleText)) {
          brailleText = text;
        } else if (Arrays.equals(brailleCells, cells)) {
          return true;
        }

        brailleCells = cells;
        writePending = true;
        logCells(brailleCells, "updated");
        return writeCells(false);
      }
    }
  }

  public final boolean write (Endpoint endpoint) {
    synchronized (endpoint) {
      int length = getLength();
      if (length == 0) return false;

      byte[] cells = new byte[length];
      CharSequence text = BrailleUtilities.setCells(cells, endpoint);
      return write(text, cells);
    }
  }

  private final boolean write (WriteElement element) {
    return write(element.text, element.cells);
  }

  public final boolean write (CharSequence text) {
    return write(new WriteElement(text));
  }

  public final void dismiss () {
    synchronized (writeDelay) {
      synchronized (this) {
        messageQueue.clear();
        messageActive = false;
        writeDelay.cancel();
        refresh();
      }
    }
  }

  public final boolean message (CharSequence text) {
    synchronized (writeDelay) {
      synchronized (this) {
        if (connect()) {
          messageQueue.offer(new WriteElement(text));
          if (messageActive) return true;
          if (writeCells(true)) return true;
        }
      }
    }

    return false;
  }

  public BrailleDevice () {
  }
}

package org.nbp.b2g.ui;

import java.util.Arrays;

import android.util.Log;

public class BrailleDevice {
  private final static String LOG_TAG = BrailleDevice.class.getName();

  public final static byte DOTS_NONE = 0;
  public final static byte DOT_1 =       0X01;
  public final static byte DOT_2 =       0X02;
  public final static byte DOT_3 =       0X04;
  public final static byte DOT_4 =       0X08;
  public final static byte DOT_5 =       0X10;
  public final static byte DOT_6 =       0X20;
  public final static byte DOT_7 =       0X40;
  public final static byte DOT_8 = (byte)0X80;

  private final Object LOCK = new Object();
  private byte[] brailleCells = null;

  private native boolean openDevice ();
  private native void closeDevice ();

  private native String getVersion ();
  private native int getCellCount ();

  private native boolean clearCells ();
  private native boolean writeCells (byte[] cells);

  public boolean open () {
    synchronized (LOCK) {
      if (brailleCells != null) return true;

      if (openDevice()) {
        int cellCount = getCellCount();

        if (cellCount > 0) {
          brailleCells = new byte[cellCount];
          Log.d(LOG_TAG, "braille cell count: " + brailleCells.length);

          String version = getVersion();
          Log.d(LOG_TAG, "braille device version: " + version);

          clearCells();
          Arrays.fill(brailleCells, DOTS_NONE);
          return true;
        }

        closeDevice();
      }
    }

    return false;
  }

  public void close () {
    synchronized (LOCK) {
      if (brailleCells != null) {
        brailleCells = null;
        closeDevice();
      }
    }
  }

  public int size () {
    synchronized (LOCK) {
      if (open()) return brailleCells.length;
      return 0;
    }
  }

  private boolean writeCells () {
    return writeCells(brailleCells);
  }

  private void setCells (Characters characters, String text) {
    int count = text.length();
    if (count > brailleCells.length) count = brailleCells.length;
    int index = 0;

    while (index < count) {
      char character = text.charAt(index);
      Byte dots = characters.getDots(character);
      brailleCells[index++] = (dots != null)? dots: ApplicationParameters.BRAILLE_CHARACTER_UNDEFINED;
    }

    while (index < brailleCells.length) brailleCells[index++] = 0;
  }

  private void setCells (Endpoint endpoint) {
    synchronized (endpoint) {
      String text = endpoint.getLineText();
      int length = text.length();

      int indent = endpoint.getLineIndent();
      if (indent > length) indent = length;
      setCells(endpoint.getCharacters(), text.substring(indent));

      if (endpoint.isEditable()) {
        int start = endpoint.getSelectionStart();
        int end = endpoint.getSelectionEnd();

        if (endpoint.isSelected(start) && endpoint.isSelected(end)) {
          int brailleStart = endpoint.getBrailleStart();
          int nextLine = length - indent + 1;

          if ((start -= brailleStart) < 0) start = 0;
          if ((end -= brailleStart) > nextLine) end = nextLine;

          if (start == end) {
            if (end < brailleCells.length) {
              brailleCells[end] |= ApplicationParameters.BRAILLE_OVERLAY_CURSOR;
            }
          } else {
            if (end > brailleCells.length) end = brailleCells.length;

            while (start < end) {
              brailleCells[start++] |= ApplicationParameters.BRAILLE_OVERLAY_SELECTED;
            }
          }
        }
      }
    }
  }

  private boolean writePending = false;
  private Timeout writeDelay = new Timeout(ApplicationParameters.BRAILLE_REWRITE_DELAY) {
    @Override
    public void run () {
      synchronized (LOCK) {
        if (writePending) {
          writePending = false;
          write();
        }
      }
    }
  };

  public boolean write () {
    synchronized (LOCK) {
      if (writeDelay.isActive()) {
        writePending = true;
        return true;
      }

      if (open()) {
        byte[] oldCells = Arrays.copyOf(brailleCells, brailleCells.length);
        setCells(Endpoints.getCurrentEndpoint());
        if (Arrays.equals(brailleCells, oldCells)) return true;

        if (writeCells()) {
          writePending = false;
          writeDelay.start();
          return true;
        }
      }
    }

    return false;
  }

  public boolean write (Endpoint endpoint, String message) {
    synchronized (LOCK) {
      if (open()) {
        writeDelay.cancel();
        setCells(endpoint.getCharacters(), message);

        if (writeCells()) {
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

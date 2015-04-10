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

  private final static Object LOCK = new Object();
  private static byte[] brailleCells = null;

  private native static boolean openDevice ();
  private native static void closeDevice ();

  private native static String getVersion ();
  private native static int getCellCount ();

  private native static boolean clearCells ();
  private native static boolean writeCells (byte[] cells);

  public static boolean open () {
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

  public static void close () {
    synchronized (LOCK) {
      if (brailleCells != null) {
        brailleCells = null;
        closeDevice();
      }
    }
  }

  public static int size () {
    synchronized (LOCK) {
      if (open()) return brailleCells.length;
      return 0;
    }
  }

  private static boolean writePending = false;
  private static Timeout writeDelay = new Timeout(ApplicationParameters.BRAILLE_UPDATE_DELAY) {
    @Override
    public void run (Object argument) {
      Endpoint endpoint = (Endpoint)argument;

      synchronized (LOCK) {
        if (writePending) {
          writePending = false;
          write(endpoint);
        }
      }
    }
  };

  public static boolean write (final Endpoint endpoint) {
    synchronized (LOCK) {
      if (writeDelay.isActive()) {
        writePending = true;
        return true;
      }

      if (open()) {
        byte[] oldCells = Arrays.copyOf(brailleCells, brailleCells.length);

        String text = endpoint.getLineText();
        int length = text.length();
        int indent = endpoint.getLineIndent();

        {
          int toIndex = 0;
          int count = length - indent;
          if (count > brailleCells.length) count = brailleCells.length;

          while (toIndex < count) {
            int fromIndex = toIndex + indent;
            char character = (fromIndex < length)? text.charAt(fromIndex): ' ';
            Byte dots = endpoint.getCharacters().getDots(character);
            brailleCells[toIndex++] = (dots != null)? dots: ApplicationParameters.BRAILLE_CHARACTER_UNDEFINED;
          }

          while (toIndex < brailleCells.length) brailleCells[toIndex++] = 0;
        }

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

        if (Arrays.equals(brailleCells, oldCells)) {
          return true;
        }

        if (writeCells(brailleCells)) {
          writePending = false;
          writeDelay.start(endpoint);
          return true;
        }
      }
    }

    return false;
  }

  private BrailleDevice () {
  }

  static {
    System.loadLibrary("UserInterface");
  }
}

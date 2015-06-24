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

  private native boolean openDevice ();
  private native void closeDevice ();

  private native String getVersion ();
  private native int getCellCount ();

  private byte[] brailleCells = null;
  private boolean writePending = false;

  public static char toBrailleCharacter (byte cell) {
    return (char)((cell & 0XFF) | 0X2800);
  }

  public static char[] toBrailleCharacters (byte[] cells) {
    if (cells == null) return null;

    int count = cells.length;
    char[] characters = new char[count];

    for (int index=0; index<count; index+=1) {
      characters[index] = toBrailleCharacter(cells[index]);
    }

    return characters;
  }

  public static String toBrailleString (byte[] cells) {
    return new String(toBrailleCharacters(cells));
  }

  public byte[] getBrailleCells () {
    return Arrays.copyOf(brailleCells, brailleCells.length);
  }

  private void logBrailleCells (String action) {
    if (ApplicationSettings.LOG_UPDATES) {
      Log.v(LOG_TAG, String.format(
        "braille cells: %s: %s",
        action, toBrailleString(brailleCells)
      ));
    }
  }

  private native boolean clearCells ();
  private native boolean writeCells (byte[] cells);

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
          Arrays.fill(brailleCells, DOTS_NONE);
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
    logBrailleCells("writing");
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
              brailleCells[end] |= ApplicationSettings.CURSOR_INDICATOR.getDots();
            }
          } else {
            if (end > brailleCells.length) end = brailleCells.length;

            while (start < end) {
              brailleCells[start++] |= ApplicationSettings.SELECTION_INDICATOR.getDots();
            }
          }
        }
      }
    }
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
        byte[] oldCells = getBrailleCells();
        setCells(Endpoints.getCurrentEndpoint());
        if (Arrays.equals(brailleCells, oldCells)) return true;
      }

      writePending = true;
      logBrailleCells("updated");
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

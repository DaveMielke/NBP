package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.LinkedHashMap;

import org.nbp.common.BitSet;

import android.util.Log;

public class BaumProtocol extends Protocol {
  private final static String LOG_TAG = BaumProtocol.class.getName();

  private final static byte ESCAPE = Characters.CHAR_ESC;

  private final static byte WRITE_CELLS     =       0X01;
  private final static byte GET_KEYS        =       0X08;
  private final static byte CURSOR_KEYS     =       0X22;
  private final static byte DISPLAY_KEYS    =       0X24;
  private final static byte ENTRY_KEYS      =       0X33;
  private final static byte JOYSTICK        =       0X34;
  private final static byte ERROR_CODE      =       0X40;
  private final static byte DEVICE_IDENTITY = (byte)0X84;
  private final static byte SERIAL_NUMBER   = (byte)0X8A;
  private final static byte BLUETOOTH_NAME  = (byte)0X8C;

  private enum InputState {
    WAITING,
    STARTED,
    ESCAPE,
    ;
  }

  private final byte[] inputBuffer = new byte[0X100];
  private InputState inputState;
  private int inputLength;
  private int inputCount;

  private final boolean send (Channel channel, byte b) {
    if (b == ESCAPE) {
      if (!channel.send(ESCAPE)) {
        return false;
      }
    }

    return channel.send(b);
  }

  private final Channel begin (byte command) {
    Channel channel = remoteEndpoint.getChannel();

    if (channel.send(ESCAPE)) {
      if (send(channel, command)) {
        return channel;
      }
    }

    return null;
  }

  private final boolean send (byte command, byte[] bytes, int end) {
    if (bytes == null) return true;
    if (end == 0) return true;

    Channel channel = begin(command);
    if (channel == null) return false;

    for (int index=0; index<end; index+=1) {
      if (!send(channel, bytes[index])) return false;
    }

    return true;
  }

  private final boolean send (byte command, byte[] bytes) {
    return send(command, bytes, bytes.length);
  }

  private final boolean sendErrorCode (int code) {
    return send(ERROR_CODE, new byte[] {(byte)code});
  }

  private final boolean sendCellCount () {
    return send(WRITE_CELLS, new byte[] {(byte)getCellCount()});
  }

  private class KeyGroup extends BitSet {
    private final byte command;

    public KeyGroup (int size, byte command) {
      super(size);
      this.command = command;
    }

    public final boolean send () {
      return BaumProtocol.this.send(command, elements);
    }

    public final boolean reset () {
      return empty()? send(): true;
    }
  }

  private final static int getCursorKeyCount () {
    int count = getCellCount();
    if (count <= 16) return count;
    return Math.max(count, 40);
  }

  private final KeyGroup cursorKeys = new KeyGroup(getCursorKeyCount(), CURSOR_KEYS);
  private final KeyGroup displayKeys = new KeyGroup(6, DISPLAY_KEYS);
  private final KeyGroup entryKeys = new KeyGroup(16, ENTRY_KEYS);
  private final KeyGroup joystick = new KeyGroup(5, JOYSTICK);

  private final KeyGroup[] keyGroups = new KeyGroup[] {
    cursorKeys,
    displayKeys,
    entryKeys,
    joystick
  };

  private final boolean sendKeys () {
    for (KeyGroup group : keyGroups) {
      if (!group.send()) return false;
    }

    return true;
  }

  private final boolean send (byte command, String string) {
    if (string == null) return true;

    int length = string.length();
    byte[] bytes = new byte[length];

    for (int index=0; index<length; index+=1) {
      char character = string.charAt(index);
      if (character > 0XFF) character = '?';
      bytes[index] = (byte)character;
    }

    return send(command, bytes);
  }

  private final boolean sendDeviceIdentity () {
    return send(DEVICE_IDENTITY, getString("Conny (NBP B2G)", 16));
  }

  private final boolean sendSerialNumber () {
    return send(SERIAL_NUMBER, getSerialNumber(8));
  }

  private final boolean sendBluetoothName () {
    return send(BLUETOOTH_NAME, getBluetoothName(14));
  }

  private final boolean writeCells () {
    int count = getCellCount();
    byte[] cells = new byte[count];

    System.arraycopy(inputBuffer, 1, cells, 0, count);
    return remoteEndpoint.write(Braille.toString(cells));
  }

  @Override
  public final void resetInput () {
    inputState = InputState.WAITING;
  }

  @Override
  public final boolean handleTimeout () {
    if (inputState != InputState.WAITING) {
      if (inputCount > 0) {
        switch (inputBuffer[0]) {
          case WRITE_CELLS:
            if (!sendCellCount()) return false;
            break;

          default:
            break;
        }
      }
    }

    return true;
  }

  private final void startInput () {
    inputState = InputState.STARTED;
    inputLength = 1;
    inputCount = 0;
  }

  private final boolean handleInput () {
    boolean ok;
    byte request = inputBuffer[0];

    switch (request) {
      case WRITE_CELLS:
        if (inputCount == 1) {
          inputLength += getCellCount();
          return false;
        }

        ok = writeCells();
        break;

      case GET_KEYS:
        ok = sendKeys();
        break;

      case DEVICE_IDENTITY:
        ok = sendDeviceIdentity();
        break;

      case SERIAL_NUMBER:
        ok = sendSerialNumber();
        break;

      case BLUETOOTH_NAME:
        ok = sendBluetoothName();
        break;

      default:
        Log.w(LOG_TAG, String.format("unsupported request: %02X", request));
        ok = sendErrorCode(0X14);
        break;
    }

    resetInput();
    return true;
  }

  @Override
  public final boolean handleInput (byte b) {
    boolean isEscape = b == ESCAPE;

    switch (inputState) {
      case WAITING:
        if (isEscape) {
          startInput();
          return false;
        }

        logIgnoredByte(b);
        return true;

      case ESCAPE:
        if (!isEscape) {
          handleTimeout();
          startInput();
          return handleInput(b);
        }

        inputState = InputState.STARTED;
        isEscape = false;

      case STARTED:
        if (isEscape) {
          inputState = InputState.ESCAPE;
          return false;
        }

        inputBuffer[inputCount++] = b;
        if (inputCount < inputLength) return false;
        return handleInput();

      default:
        Log.w(LOG_TAG, ("unsupported input state: " + inputState.name()));
        break;
    }

    resetInput();
    return true;
  }

  @Override
  public void resetKeys () {
    for (KeyGroup group : keyGroups) group.reset();
  }

  private static class KeyReference {
    public final KeyGroup group;
    public final int number;

    public KeyReference (KeyGroup group, int number) {
      this.group = group;
      this.number = number;
    }
  }

  private final Map<Integer, KeyReference> navigationKeys = new LinkedHashMap<Integer, KeyReference>();

  private final void addNavigationKey (int mask, KeyGroup group, int number) {
    navigationKeys.put(mask, new KeyReference(group, number));
  }

  private final void addNavigationKeys () {
    addNavigationKey(KeyMask.BACKWARD, entryKeys,  0);
    addNavigationKey(KeyMask.FORWARD , entryKeys,  1);
    addNavigationKey(KeyMask.SPACE   , entryKeys,  2);
    addNavigationKey(KeyMask.DOT_1   , entryKeys,  8);
    addNavigationKey(KeyMask.DOT_2   , entryKeys,  9);
    addNavigationKey(KeyMask.DOT_3   , entryKeys, 10);
    addNavigationKey(KeyMask.DOT_4   , entryKeys, 11);
    addNavigationKey(KeyMask.DOT_5   , entryKeys, 12);
    addNavigationKey(KeyMask.DOT_6   , entryKeys, 13);
    addNavigationKey(KeyMask.DOT_7   , entryKeys, 14);
    addNavigationKey(KeyMask.DOT_8   , entryKeys, 15);

    addNavigationKey(KeyMask.DPAD_UP    , joystick, 0);
    addNavigationKey(KeyMask.DPAD_LEFT  , joystick, 1);
    addNavigationKey(KeyMask.DPAD_DOWN  , joystick, 2);
    addNavigationKey(KeyMask.DPAD_RIGHT , joystick, 3);
    addNavigationKey(KeyMask.DPAD_CENTER, joystick, 4);
  }

  @Override
  public final int handleNavigationKeys (int keyMask, boolean press) {
    for (Integer mask : navigationKeys.keySet()) {
      if ((keyMask & mask) != 0) {
        keyMask &= ~mask;

        KeyReference ref = navigationKeys.get(mask);
        if (ref.group.change(ref.number, press)) ref.group.send();
      }
    }

    return keyMask;
  }

  @Override
  public final boolean handleCursorKey (int keyNumber, boolean press) {
    KeyGroup group = cursorKeys;
    if (keyNumber >= group.size) return false;

    if (group.change(keyNumber, press)) group.send();
    return true;
  }

  public BaumProtocol (RemoteEndpoint endpoint) {
    super(endpoint);

    addNavigationKeys();
  }
}

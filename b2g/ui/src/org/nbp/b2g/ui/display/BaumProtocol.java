package org.nbp.b2g.ui.display;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.LinkedHashMap;

import org.nbp.common.BitSet;

import android.util.Log;

public class BaumProtocol extends Protocol {
  private final static String LOG_TAG = BaumProtocol.class.getName();

  private final static int INPUT_BUFFER_SIZE = 0X100;
  private final static byte ESCAPE = Characters.CHAR_ESC;

  private final static byte WRITE_CELLS     =       0X01;
  private final static byte GET_KEYS        =       0X08;
  private final static byte DEVICE_IDENTITY = (byte)0X84;
  private final static byte SERIAL_NUMBER   = (byte)0X8A;
  private final static byte BLUETOOTH_NAME  = (byte)0X8C;

  private enum InputState {
    WAITING,
    STARTED,
    ESCAPE,
    ;
  }

  private final byte[] inputBuffer = new byte[INPUT_BUFFER_SIZE];
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

  private final Channel begin (int command) {
    Channel channel = getChannel();

    if (channel.send(ESCAPE)) {
      if (send(channel, (byte)command)) {
        return channel;
      }
    }

    return null;
  }

  private final boolean send (int command, byte[] bytes) {
    if (bytes == null) return true;
    if (bytes.length == 0) return true;

    Channel channel = begin(command);
    if (channel == null) return false;

    for (int index=0; index<bytes.length; index+=1) {
      if (!send(channel, bytes[index])) return false;
    }

    return true;
  }

  private final boolean sendErrorCode (int code) {
    return send(0X40, new byte[] {(byte)code});
  }

  private final boolean sendCellCount () {
    return send(WRITE_CELLS, new byte[] {(byte)getCellCount()});
  }

  private class KeyGroup extends BitSet {
    private final int command;

    public KeyGroup (int size, int command) {
      super(size);
      this.command = command;
    }

    public final boolean send () {
      return BaumProtocol.this.send(command, elements)
          && flushOutput();
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

  private class CursorKeys extends KeyGroup {
    public CursorKeys () {
      super(getCursorKeyCount(), 0X22);
    }
  }

  private class DisplayKeys extends KeyGroup {
    public final static byte D1 = 0;
    public final static byte D2 = 1;
    public final static byte D3 = 2;
    public final static byte D4 = 3;
    public final static byte D5 = 4;
    public final static byte D6 = 5;

    public DisplayKeys () {
      super(6, 0X24);
    }
  }

  private class EntryKeys extends KeyGroup {
    public final static byte B9  =  0;
    public final static byte B10 =  1;
    public final static byte B11 =  2;

    public final static byte F1  =  4;
    public final static byte F2  =  5;
    public final static byte F3  =  6;
    public final static byte F4  =  7;

    public final static byte B1  =  8;
    public final static byte B2  =  9;
    public final static byte B3  = 10;
    public final static byte B4  = 11;
    public final static byte B5  = 12;
    public final static byte B6  = 13;
    public final static byte B7  = 14;
    public final static byte B8  = 15;

    public EntryKeys () {
      super(16, 0X33);
    }
  }

  private class JoystickPositions extends KeyGroup {
    public final static byte UP    = 0;
    public final static byte LEFT  = 1;
    public final static byte DOWN  = 2;
    public final static byte RIGHT = 3;
    public final static byte PRESS = 4;

    public JoystickPositions () {
      super(5, 0X34);
    }
  }

  private final KeyGroup cursorKeys = new CursorKeys();
  private final KeyGroup displayKeys = new DisplayKeys();
  private final KeyGroup entryKeys = new EntryKeys();
  private final KeyGroup joystickPositions = new JoystickPositions();

  private final KeyGroup[] keyGroups = new KeyGroup[] {
    cursorKeys,
    displayKeys,
    entryKeys,
    joystickPositions
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
      if (character > BYTE_MASK) character = '?';
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
    return write(inputBuffer, 1);
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
    addNavigationKey(KeyMask.BACKWARD, entryKeys, EntryKeys.B9);
    addNavigationKey(KeyMask.FORWARD , entryKeys, EntryKeys.B10);
    addNavigationKey(KeyMask.SPACE   , entryKeys, EntryKeys.B11);
    addNavigationKey(KeyMask.DOT_1   , entryKeys, EntryKeys.B1);
    addNavigationKey(KeyMask.DOT_2   , entryKeys, EntryKeys.B2);
    addNavigationKey(KeyMask.DOT_3   , entryKeys, EntryKeys.B3);
    addNavigationKey(KeyMask.DOT_4   , entryKeys, EntryKeys.B4);
    addNavigationKey(KeyMask.DOT_5   , entryKeys, EntryKeys.B5);
    addNavigationKey(KeyMask.DOT_6   , entryKeys, EntryKeys.B6);
    addNavigationKey(KeyMask.DOT_7   , entryKeys, EntryKeys.B7);
    addNavigationKey(KeyMask.DOT_8   , entryKeys, EntryKeys.B8);

    addNavigationKey(KeyMask.DPAD_UP    , joystickPositions, JoystickPositions.UP);
    addNavigationKey(KeyMask.DPAD_LEFT  , joystickPositions, JoystickPositions.LEFT);
    addNavigationKey(KeyMask.DPAD_DOWN  , joystickPositions, JoystickPositions.DOWN);
    addNavigationKey(KeyMask.DPAD_RIGHT , joystickPositions, JoystickPositions.RIGHT);
    addNavigationKey(KeyMask.DPAD_CENTER, joystickPositions, JoystickPositions.PRESS);
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

  public BaumProtocol (DisplayEndpoint endpoint) {
    super(endpoint);

    addNavigationKeys();
  }
}

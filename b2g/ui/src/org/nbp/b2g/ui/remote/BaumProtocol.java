package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.LinkedHashMap;

import org.nbp.common.Tones;
import org.nbp.common.CharacterUtilities;

import android.util.Log;

public class BaumProtocol extends Protocol {
  private final static String LOG_TAG = BaumProtocol.class.getName();

  private final static byte ESCAPE = CharacterUtilities.CHAR_ESC;

  private final static byte WRITE_CELLS     =       0X01;
  private final static byte CURRENT_KEYS    =       0X08;
  private final static byte DEVICE_IDENTITY = (byte)0X84;
  private final static byte SERIAL_NUMBER   = (byte)0X8A;
  private final static byte BLUETOOTH_NAME  = (byte)0X8C;

  private enum InputState {
    WAITING,
    STARTED,
    ESCAPE,
    ; // end of enumeration
  }

  private final byte[] inputBuffer = new byte[2 + getCellCount()];
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

  private class KeyGroup extends BitGroup {
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
    return writeCells(inputBuffer, 1);
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

      case CURRENT_KEYS:
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
        /* fall through */

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

  private static class KeyDescriptor {
    public final KeyGroup group;
    public final int number;

    public KeyDescriptor (KeyGroup group, int number) {
      this.group = group;
      this.number = number;
    }
  }

  private final Map<Integer, KeyDescriptor> keyMap =
  new LinkedHashMap<Integer, KeyDescriptor>();

  private final void mapKey (int index, KeyDescriptor descriptor) {
    keyMap.put(index, descriptor);
  }

  private final void mapKey (int index, KeyGroup group, int number) {
    mapKey(index, new KeyDescriptor(group, number));
  }

  private final void mapKey (int index) {
    mapKey(index, null);
  }

  private final void mapCommonKeys () {
    mapKey(KeySet.PAD_UP    , joystickPositions, JoystickPositions.UP);
    mapKey(KeySet.PAD_LEFT  , joystickPositions, JoystickPositions.LEFT);
    mapKey(KeySet.PAD_DOWN  , joystickPositions, JoystickPositions.DOWN);
    mapKey(KeySet.PAD_RIGHT , joystickPositions, JoystickPositions.RIGHT);
    mapKey(KeySet.PAD_CENTER, joystickPositions, JoystickPositions.PRESS);
  }

  private final void mapDisplayKeys () {
    mapKey(KeySet.DOT_1, displayKeys, DisplayKeys.D1);
    mapKey(KeySet.DOT_2, displayKeys, DisplayKeys.D2);
    mapKey(KeySet.DOT_3, displayKeys, DisplayKeys.D3);
    mapKey(KeySet.DOT_4, displayKeys, DisplayKeys.D4);
    mapKey(KeySet.DOT_5, displayKeys, DisplayKeys.D5);
    mapKey(KeySet.DOT_6, displayKeys, DisplayKeys.D6);

    mapKey(KeySet.PAN_BACKWARD, entryKeys, EntryKeys.F1);
    mapKey(KeySet.DOT_7   ,     entryKeys, EntryKeys.F2);
    mapKey(KeySet.DOT_8   ,     entryKeys, EntryKeys.F3);
    mapKey(KeySet.PAN_FORWARD , entryKeys, EntryKeys.F4);

    mapKey(KeySet.SPACE);
  }

  private final void mapEntryKeys () {
    mapKey(KeySet.DOT_1, entryKeys, EntryKeys.B1);
    mapKey(KeySet.DOT_2, entryKeys, EntryKeys.B2);
    mapKey(KeySet.DOT_3, entryKeys, EntryKeys.B3);
    mapKey(KeySet.DOT_4, entryKeys, EntryKeys.B4);
    mapKey(KeySet.DOT_5, entryKeys, EntryKeys.B5);
    mapKey(KeySet.DOT_6, entryKeys, EntryKeys.B6);
    mapKey(KeySet.DOT_7, entryKeys, EntryKeys.B7);
    mapKey(KeySet.DOT_8, entryKeys, EntryKeys.B8);

    mapKey(KeySet.PAN_BACKWARD, entryKeys, EntryKeys.B9);
    mapKey(KeySet.PAN_FORWARD , entryKeys, EntryKeys.B10);
    mapKey(KeySet.SPACE   ,     entryKeys, EntryKeys.B11);
  }

  private abstract class PendingSpaceAction {
    public abstract void performAction ();
    public abstract String getMessage ();
  }

  private final Map<Integer, PendingSpaceAction> pendingSpaceActions =
  new LinkedHashMap<Integer, PendingSpaceAction>();

  private final void mapPendingSpaceAction (int key, PendingSpaceAction action) {
    pendingSpaceActions.put(key, action);
  }

  private final void mapPendingSpaceActions () {
    mapPendingSpaceAction(
      KeySet.PAN_BACKWARD,
      new PendingSpaceAction() {
        @Override
        public final void performAction () {
          mapDisplayKeys();
        }

        @Override
        public final String getMessage () {
          return "navigation mode";
        }
      }
    );

    mapPendingSpaceAction(
      KeySet.PAN_FORWARD,
      new PendingSpaceAction() {
        @Override
        public final void performAction () {
          mapEntryKeys();
        }

        @Override
        public final String getMessage () {
          return "keyboard mode";
        }
      }
    );
  }

  private int pressedKeyCount = 0;
  private KeyDescriptor pendingKeyPress = null;
  private PendingSpaceAction pendingSpaceAction = null;

  private final void handleKeyEvent (KeyGroup group, int number, boolean press) {
    if (group.set(number, press)) {
      group.send();

      if (press) {
        pressedKeyCount += 1;
      } else {
        pressedKeyCount -= 1;
      }
    }
  }

  private final void handleKeyEvent (KeyDescriptor key, boolean press) {
    handleKeyEvent(key.group, key.number, press);
  }

  private final boolean handleKeyEvent (int index, boolean press) {
    KeyDescriptor key = keyMap.get(index);

    if (press) {
      if (pressedKeyCount == 0) {
        if (pendingKeyPress == null) {
          PendingSpaceAction action = pendingSpaceActions.get(index);

          if (action != null) {
            pendingSpaceAction = action;
            pendingKeyPress = key;
            return true;
          }
        } else if (index == KeySet.SPACE) {
          PendingSpaceAction action = pendingSpaceAction;
          pendingSpaceAction = null;
          pendingKeyPress = null;

          message(action.getMessage());
          action.performAction();
          return true;
        }
      }
    } else if (pendingKeyPress != null) {
      handleKeyEvent(pendingKeyPress, true);
      pendingKeyPress = null;
      pendingSpaceAction = null;
    }

    if (key != null) {
      handleKeyEvent(key, press);
    } else if (!keyMap.containsKey(index)) {
      return false;
    } else if (press) {
      Tones.beep();
    }

    return true;
  }

  @Override
  public final boolean handleNavigationKeyEvent (int key, boolean press) {
    return handleKeyEvent(key, press);
  }

  @Override
  public final boolean handleCursorKeyEvent (int key, boolean press) {
    KeyGroup group = cursorKeys;
    if (key >= group.size) return false;

    handleKeyEvent(group, key, press);
    return true;
  }

  @Override
  public final void resetKeys () {
    for (KeyGroup group : keyGroups) group.reset();

    pressedKeyCount = 0;
    pendingKeyPress = null;
    pendingSpaceAction = null;
  }

  public BaumProtocol () {
    super();

    mapPendingSpaceActions();
    mapCommonKeys();
    mapDisplayKeys();
  }
}

package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class BaumProtocol extends Protocol {
  private final static String LOG_TAG = BaumProtocol.class.getName();

  private final static byte ESCAPE = Characters.CHAR_ESC;

  private final static byte WRITE_CELLS     =       0X01;
  private final static byte GET_KEYS        =       0X08;
  private final static byte DEVICE_IDENTITY = (byte)0X84;
  private final static byte SERIAL_NUMBER   = (byte)0X8A;

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

  private final boolean write (Channel channel, byte b) {
    if (b == ESCAPE) {
      if (!channel.write(ESCAPE)) {
        return false;
      }
    }

    return channel.write(b);
  }

  private final Channel begin (byte response) {
    Channel channel = remoteEndpoint.getChannel();

    if (channel.write(ESCAPE)) {
      if (write(channel, response)) {
        return channel;
      }
    }

    return null;
  }

  private final boolean write (byte response, byte[] bytes) {
    Channel channel = begin(response);
    if (channel == null) return false;

    for (byte b : bytes) {
      if (!write(channel, b)) return false;
    }

    return true;
  }

  private final boolean write (byte response, String string) {
    int length = string.length();
    byte[] bytes = new byte[length];

    for (int index=0; index<length; index+=1) {
      char character = string.charAt(index);
      if (character > 0XFF) character = '?';
      bytes[index] = (byte)character;
    }

    return write(response, bytes);
  }

  private final boolean writeDeviceIdentity () {
    return write(DEVICE_IDENTITY, getString("Conny", 16));
  }

  private final boolean writeSerialNumber () {
    return write(SERIAL_NUMBER, getSerialNumber(8));
  }

  private final boolean writeCellCount () {
    return write((byte)WRITE_CELLS, new byte[] {(byte)getCellCount()});
  }

  private final boolean writeCells () {
    int count = getCellCount();
    byte[] cells = new byte[count];

    System.arraycopy(inputBuffer, 1, cells, 0, count);
    return remoteEndpoint.write(Braille.toString(cells));
  }

  @Override
  public final boolean resetInput (boolean timeout) {
    if (timeout) {
      if (inputState == InputState.STARTED) {
        if (inputCount > 0) {
          switch (inputBuffer[0]) {
            case WRITE_CELLS:
              if (!writeCellCount()) return false;
              break;

            default:
              break;
          }
        }
      }
    }

    inputState = InputState.WAITING;
    return true;
  }

  private final boolean handleInput () {
    byte request = inputBuffer[0];

    switch (request) {
      case WRITE_CELLS:
        if (inputCount == 1) {
          inputLength += getCellCount();
          return false;
        }

        writeCells();
        break;

      case GET_KEYS:
        break;

      case DEVICE_IDENTITY:
        writeDeviceIdentity();
        break;

      case SERIAL_NUMBER:
        writeSerialNumber();
        break;

      default:
        Log.w(LOG_TAG, String.format("unsupported request: %02X", request));
        break;
    }

    resetInput(false);
    return true;
  }

  @Override
  public final boolean handleInput (byte b) {
    boolean isEscape = b == ESCAPE;

    switch (inputState) {
      case WAITING:
        if (isEscape) {
          inputState = InputState.STARTED;
          inputLength = 1;
          inputCount = 0;
          return false;
        }

        logIgnoredByte(b);
        return true;

      case ESCAPE:
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

    resetInput(false);
    return true;
  }

  public BaumProtocol (RemoteEndpoint endpoint) {
    super(endpoint);
  }
}

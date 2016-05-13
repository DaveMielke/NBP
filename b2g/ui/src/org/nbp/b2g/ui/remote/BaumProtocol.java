package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class BaumProtocol extends Protocol {
  private final static String LOG_TAG = BaumProtocol.class.getName();

  private final static byte ESCAPE = Characters.CHAR_ESC;

  private enum InputState {
    WAITING,
    STARTED,
    ESCAPE,
    ;
  }

  private final byte[] inputBuffer = new byte[0X10];
  private InputState inputState;
  private int inputLength;
  private int inputCount;

  @Override
  public final void resetInput (boolean timeout) {
    inputState = InputState.WAITING;
  }

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

  private final boolean writeSerialNumber () {
    return write((byte)0X8A, getSerialNumber(8));
  }

  private final boolean handleInput () {
    byte request = inputBuffer[0];

    switch (request) {
      case (byte)0X8A:
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
        break;
    }

    resetInput(false);
    return true;
  }

  public BaumProtocol (RemoteEndpoint endpoint) {
    super(endpoint);
  }
}

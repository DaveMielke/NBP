package org.nbp.ipaws;

import android.util.Log;

import java.util.concurrent.TimeUnit;

public class PingHandler extends CommandHandler {
  private final static String LOG_TAG = PingHandler.class.getName();

  public PingHandler (SessionOperations operations) {
    super(operations);
  }

  @Override
  public final boolean handleOperands (String string) {
    String[] operands = getOperands(string, 3);
    int count = operands.length;
    int index = 0;

    String identifier = "";
    if (index < count) identifier = operands[index++];

    String next = "";
    if (index < count) next = operands[index++];

    if (!next.isEmpty()) {
      try {
        long timeout = Integer.parseInt(next);

        if (timeout < 1) {
          throw new NumberFormatException("value not positive");
        }

        timeout = TimeUnit.SECONDS.toMillis(timeout);
        timeout += ApplicationParameters.PING_RECEIVE_TIMEOUT;
        setReadTimeout(timeout);
      } catch (NumberFormatException exception) {
        Log.w(LOG_TAG, ("invalid time till next ping: " + next));
      }
    }

    if (identifier.isEmpty()) return true;
    StringBuilder command = new StringBuilder("pong");
    command.append(' ');
    command.append(identifier);
    return writeCommand(command);
  }
}

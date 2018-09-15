package org.nbp.ipaws;

import android.util.Log;

public class PingHandler extends OperandsHandler {
  private final static String LOG_TAG = PingHandler.class.getName();

  private final CommandWriter commandWriter;

  public PingHandler (CommandWriter writer) {
    super();
    commandWriter = writer;
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

        timeout += 5 * 60;
        timeout *= 1000;
        commandWriter.setTimeout(timeout);
      } catch (NumberFormatException exception) {
        Log.w(LOG_TAG, ("invalid time till next ping: " + next));
      }
    }

    if (identifier.isEmpty()) return true;
    StringBuilder command = new StringBuilder("pong");
    command.append(' ');
    command.append(identifier);
    return commandWriter.writeCommand(command);
  }
}

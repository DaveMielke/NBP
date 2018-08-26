package org.nbp.ipaws;

public class PingHandler extends ResponseHandler {
  private final CommandWriter commandWriter;

  public PingHandler (CommandWriter writer) {
    super();
    commandWriter = writer;
  }

  @Override
  public final boolean handleResponse (String response) {
    String[] operands = getOperands(response, 2);
    int count = operands.length;
    int index = 0;

    if (index == count) return true;
    String identifier = operands[index++];
    if (identifier.isEmpty()) return true;

    StringBuilder command = new StringBuilder("pong");
    command.append(' ');
    command.append(identifier);
    return commandWriter.writeCommand(command);
  }
}

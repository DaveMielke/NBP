package org.nbp.ipaws;

public abstract class CommandHandler extends OperandsHandler {
  private final SessionOperations sessionOperations;

  protected CommandHandler (SessionOperations operations) {
    super();
    sessionOperations = operations;
  }

  protected final boolean writeCommand (StringBuilder command) {
    return sessionOperations.writeCommand(command);
  }

  protected final String readLine () {
    return sessionOperations.readLine();
  }

  protected final boolean setReadTimeout (long milliseconds) {
    return sessionOperations.setReadTimeout(milliseconds);
  }
}

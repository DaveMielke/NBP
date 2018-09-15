package org.nbp.ipaws;

public class PongHandler extends CommandHandler {
  public PongHandler (SessionOperations operations) {
    super(operations);
  }

  @Override
  public final boolean handleOperands (String string) {
    return true;
  }
}

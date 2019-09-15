package org.nbp.common.dictionary;

public class CommandException extends DictionaryException {
  private final String exceptionData;

  public CommandException (String message, String data) {
    super(message);
    exceptionData = data;
  }

  public CommandException (String message) {
    this(message, null);
  }

  public final String getData () {
    return exceptionData;
  }
}

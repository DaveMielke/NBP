package org.nbp.common.dictionary;

public class ResponseException extends DictionaryException {
  private final String exceptionData;

  public ResponseException (String message, String data) {
    super(message);
    exceptionData = data;
  }

  public ResponseException (String message) {
    this(message, null);
  }

  public final String getData () {
    return exceptionData;
  }
}

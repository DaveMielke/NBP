package com.duxburysystems;

public class BrailleTranslationException extends RuntimeException {
  private final int errorCode;

  public BrailleTranslationException (int error) {
    super(BrailleTranslationErrors.getMessage(error));
    errorCode = error;
  }

  public final int getErrorCode () {
    return errorCode;
  }
}

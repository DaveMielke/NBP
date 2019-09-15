package org.nbp.common.dictionary;

public class ControlCharacterException extends DictionaryException {
  public ControlCharacterException (char character) {
    super(String.format("U+%04X", (int)character));
  }
}

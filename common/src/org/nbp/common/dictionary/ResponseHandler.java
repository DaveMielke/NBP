package org.nbp.common.dictionary;

public interface ResponseHandler {
  public boolean handleResponse (int code, DictionaryOperands operands);
}

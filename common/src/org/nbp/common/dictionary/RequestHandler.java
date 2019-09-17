package org.nbp.common.dictionary;

public interface RequestHandler {
  public boolean handleResponse (int code, DictionaryOperands operands);
  public void setFinished ();
  public boolean isFinal ();
}

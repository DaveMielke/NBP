package org.nbp.common;

public class ErrnoException extends RuntimeException {
  public ErrnoException (String message) {
    super(message);
  }
}

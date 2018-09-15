package org.nbp.ipaws;

public interface SessionOperations {
  public boolean writeCommand (StringBuilder command);
  public String readLine ();
  public boolean setReadTimeout (long milliseconds);
}

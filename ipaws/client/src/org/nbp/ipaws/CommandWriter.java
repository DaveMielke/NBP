package org.nbp.ipaws;

public interface CommandWriter {
  public boolean writeCommand (String command);
  public boolean writeCommand (StringBuilder command);
}

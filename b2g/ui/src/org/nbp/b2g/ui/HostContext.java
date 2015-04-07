package org.nbp.b2g.ui;

public class HostContext extends Context {
  private final static String[] keysFileNames = new String[] {
    "nabcc", "navigation", "developer"
  };

  public HostContext () {
    super(keysFileNames);
  }
}

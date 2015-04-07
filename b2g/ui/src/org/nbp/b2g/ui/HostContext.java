package org.nbp.b2g.ui;

public class HostContext extends Context {
  private final static String[] keysFileNames = new String[] {
    "nabcc", "navigation", "developer"
  };

  @Override
  protected String[] getKeysFileNames () {
    return keysFileNames;
  }

  public HostContext () {
    super();
  }
}

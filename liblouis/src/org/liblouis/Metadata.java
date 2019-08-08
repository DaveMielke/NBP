package org.liblouis;

public abstract class Metadata {
  public native static String findTable (String query);
  public native static String getValueForKey (String table, String key);

  private Metadata () {
  }
}

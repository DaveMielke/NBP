package org.nbp.ipaws;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static String SERVER_NAME = "Mielke.cc";
  public final static int SERVER_PORT = 14216;

  public final static int RECONNECT_INITIAL_DELAY = 1; // seconds
  public final static int RECONNECT_MAXIMUM_DELAY = 300; // seconds

  public final static String CHARACTER_ENCODING = "UTF8";
}

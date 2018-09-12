package org.nbp.ipaws;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static int SERVER_PORT = 14216;

  public final static int RECONNECT_INITIAL_DELAY = 1; // seconds
  public final static int RECONNECT_MAXIMUM_DELAY = 300; // seconds

  public final static long PING_INTERVAL = 29 * 60 * 1000; // milliseconds
  public final static long RESPONSE_TIMEOUT = 1000; // milliseconds

  public final static String CHARACTER_ENCODING = "UTF8";

  public final static String TIME_FORMAT_24;
  public final static String TIME_FORMAT_12;

  static {
    String suffix = " EEE MMM d yyyy zzz";
    TIME_FORMAT_24 = "HH:mm" + suffix;
    TIME_FORMAT_12 = "h:mma" + suffix;
  }
}

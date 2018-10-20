package org.nbp.ipaws;

import java.util.concurrent.TimeUnit;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static int SERVER_PORT = 14216;

  public final static long RECONNECT_INITIAL_DELAY = TimeUnit.SECONDS.toMillis(1);
  public final static long RECONNECT_MAXIMUM_DELAY = TimeUnit.MINUTES.toMillis(5);

  public final static long PING_SEND_INTERVAL = TimeUnit.MINUTES.toMillis(29);
  public final static long PING_RECEIVE_TIMEOUT = TimeUnit.MINUTES.toMillis(5);
  public final static long RESPONSE_TIMEOUT = TimeUnit.SECONDS.toMillis(1);

  public final static long TTS_RETRY_DELAY = TimeUnit.SECONDS.toMillis(5);

  public final static String CHARACTER_ENCODING = "UTF8";

  public final static String TIME_FORMAT_24;
  public final static String TIME_FORMAT_12;

  static {
    String suffix = " EEE MMM d yyyy zzz";
    TIME_FORMAT_24 = "HH:mm" + suffix;
    TIME_FORMAT_12 = "h:mma" + suffix;
  }
}

package org.nbp.ipaws;

public abstract class ApplicationSettings {
  private ApplicationSettings () {
  }

  public volatile static boolean ALERT_MONITOR = ApplicationDefaults.ALERT_MONITOR;

  public volatile static String SERVER_NAME = ApplicationDefaults.SERVER_NAME;
}

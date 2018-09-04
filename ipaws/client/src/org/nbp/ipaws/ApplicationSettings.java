package org.nbp.ipaws;

public abstract class ApplicationSettings {
  private ApplicationSettings () {
  }

  public volatile static boolean ALERT_MONITOR = ApplicationDefaults.ALERT_MONITOR;

  public volatile static String PRIMARY_SERVER = ApplicationDefaults.PRIMARY_SERVER;
  public volatile static String SECONDARY_SERVER = ApplicationDefaults.SECONDARY_SERVER;
}

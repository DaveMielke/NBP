package org.nbp.ipaws;

public abstract class ApplicationDefaults {
  private ApplicationDefaults () {
  }

  public final static boolean ALERT_MONITOR = true;
  public final static boolean SPEAK_ALERTS = true;

  public final static String PRIMARY_SERVER = "";
  public final static String SECONDARY_SERVER = "echozio.ca";
}

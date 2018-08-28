package org.nbp.ipaws;

public abstract class ApplicationSettings {
  private ApplicationSettings () {
  }

  public volatile static String SERVER_NAME = ApplicationDefaults.SERVER_NAME;
}

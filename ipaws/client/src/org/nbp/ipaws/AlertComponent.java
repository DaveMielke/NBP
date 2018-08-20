package org.nbp.ipaws;

import android.content.Context;
import java.io.File;

public class AlertComponent {
  protected AlertComponent () {
  }

  public static Context getContext () {
    return AlertApplication.getAlertApplication();
  }

  public static File getFilesDirectory (String name, int mode) {
    return getContext().getDir(name, mode);
  }

  public static File getFilesDirectory (String name) {
    return getFilesDirectory(name, 0);
  }

  public static File getFilesDirectory () {
    return getContext().getFilesDir();
  }

  public static File getAlertsDirectory () {
    return getFilesDirectory("alerts");
  }
}

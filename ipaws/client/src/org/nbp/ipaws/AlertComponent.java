package org.nbp.ipaws;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class AlertComponent {
  protected AlertComponent () {
  }

  public static Context getContext () {
    return AlertApplication.getAlertApplication();
  }

  public static SharedPreferences getSharedPreferences (String name, int mode) {
    return getContext().getSharedPreferences(name, mode);
  }

  public static SharedPreferences getSharedPreferences (String name) {
    return getSharedPreferences(name, 0);
  }

  public final static String SETTING_REQUESTED_AREAS = "requested-areas";

  public static SharedPreferences getSettings () {
    return getSharedPreferences("settings");
  }

  public static File getFilesDirectory () {
    return getContext().getFilesDir();
  }

  public static File getFilesDirectory (String name, int mode) {
    return getContext().getDir(name, mode);
  }

  public static File getFilesDirectory (String name) {
    return getFilesDirectory(name, 0);
  }

  public static File getAlertsDirectory () {
    return getFilesDirectory("alerts");
  }
}

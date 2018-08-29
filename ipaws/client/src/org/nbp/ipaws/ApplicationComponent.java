package org.nbp.ipaws;

import org.nbp.common.CommonContext;
import android.content.Context;
import android.content.res.Resources;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Set;

import java.io.File;

public class ApplicationComponent {
  protected ApplicationComponent () {
  }

  public static Context getContext () {
    return CommonContext.getContext();
  }

  public static Resources getResources () {
    return getContext().getResources();
  }

  public static String getString (int resource) {
    return getContext().getString(resource);
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

  public static Set<String> getRequestedAreas () {
    return getSettings().getStringSet(SETTING_REQUESTED_AREAS, Collections.EMPTY_SET);
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
}

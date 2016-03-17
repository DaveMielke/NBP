package org.nbp.calculator;

import org.nbp.common.CommonContext;
import android.content.Context;
import android.content.SharedPreferences;

public abstract class SavedSettings {
  public final static String DEGREES = "degrees";

  public final static String RESULT = "result";
  public final static String EXPRESSION = "expression";
  public final static String START = "start";
  public final static String END = "end";

  private static Context getContext () {
    return CommonContext.getContext();
  }

  private static SharedPreferences getSettings () {
    return getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
  }

  public final static boolean remove (String name) {
    SharedPreferences settings = getSettings();
    if (!settings.contains(name)) return false;

    settings.edit().remove(name).apply();
    return true;
  }

  public final static void set (String name, String value) {
    getSettings().edit().putString(name, value).apply();
  }

  public final static String get (String name, String defaultValue) {
    return getSettings().getString(name, defaultValue);
  }

  public final static void set (String name, boolean value) {
    getSettings().edit().putBoolean(name, value).apply();
  }

  public final static boolean get (String name, boolean defaultValue) {
    return getSettings().getBoolean(name, defaultValue);
  }

  public final static void set (String name, int value) {
    getSettings().edit().putInt(name, value).apply();
  }

  public final static int get (String name, int defaultValue) {
    return getSettings().getInt(name, defaultValue);
  }

  public final static void set (String name, long value) {
    getSettings().edit().putLong(name, value).apply();
  }

  public final static long get (String name, long defaultValue) {
    return getSettings().getLong(name, defaultValue);
  }

  public final static void set (String name, float value) {
    getSettings().edit().putFloat(name, value).apply();
  }

  public final static float get (String name, float defaultValue) {
    return getSettings().getFloat(name, defaultValue);
  }

  public final static void set (String name, double value) {
    getSettings().edit().putString(name, Double.toString(value)).apply();
  }

  public final static double get (String name, double defaultValue) {
    String string = getSettings().getString(name, null);
    return (string != null)? Double.valueOf(string): defaultValue;
  }

  public final static boolean getDegrees () {
    return get(DEGREES, DefaultSettings.DEGREES);
  }

  public final static double getResult () {
    return get(RESULT, Double.NaN);
  }

  private SavedSettings () {
  }
}

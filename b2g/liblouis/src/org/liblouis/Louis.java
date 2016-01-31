package org.liblouis;

import android.util.Log;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.InputStream;

public final class Louis {
  private final static String LOG_TAG = Louis.class.getName();

  private final static String LIBRARY_NAME = "louis";
  private final static String LIBRARY_VERSION;
  public static native String getVersion ();

  static {
    System.loadLibrary(LIBRARY_NAME);

    LIBRARY_VERSION = getVersion();
    Log.i(LOG_TAG, "liblouis version: " + LIBRARY_VERSION);
  }

  public static Object NATIVE_LOCK = new Object();
  public static native void releaseMemory ();
  public static native String getDataPath ();
  public static native void setDataPath (String path);
  private static native boolean compileTranslationTable (String table);
  private static native void setLogLevel (char character);

  public enum LogLevel {
    ALL  ('A'),
    DEBUG('D'),
    INFO ('I'),
    WARN ('W'),
    ERROR('E'),
    FATAL('F'),
    OFF  ('O'),
    ; // end of enumeration

    private final char character;

    LogLevel (char character) {
      this.character = character;
    }

    public final char getCharacter () {
      return character;
    }
  }

  public static void setLogLevel (LogLevel level) {
    setLogLevel(level.getCharacter());
  }

  private final static Object INITIALIZATION_LOCK = new Object();
  private static Context currentContext = null;
  private static File dataDirectory = null;

  private static void requireInitialization () {
    synchronized (INITIALIZATION_LOCK) {
      if (currentContext == null) {
        throw new IllegalStateException("not initialized yet");
      }
    }
  }

  public static Context getContext () {
    requireInitialization();
    return currentContext;
  }

  public static File getDataDirectory () {
    requireInitialization();
    return dataDirectory;
  }

  private static SharedPreferences getSharedPreferences () {
    return PreferenceManager.getDefaultSharedPreferences(currentContext);
  }

  private static void removeFile (File file) {
    if (file.isDirectory()) {
      file.setWritable(true, true);

      for (String name : file.list()) {
        removeFile(new File(file, name));
      }
    }

    file.delete();
  }

  private static void extractAssets (AssetManager assets, String asset, File location) {
    try {
      String[] names = assets.list(asset);
      boolean isDirectory = names.length > 0;
      String path = location.getAbsolutePath();

      if (isDirectory) {
        if (!location.exists()) {
          if (!location.mkdir()) {
            Log.w(LOG_TAG, "directory not created: " + path);
            return;
          }
        } else if (!location.isDirectory()) {
          Log.w(LOG_TAG, "not a directory: " + path);
          return;
        }

        for (String name : names) {
          extractAssets(assets, new File(asset, name).getPath(), new File(location, name));
        }
      } else {
        InputStream input = assets.open(asset);
        OutputStream output = new FileOutputStream(location);
        byte[] buffer = new byte[0X1000];

        for (int count; ((count = input.read(buffer)) > 0); ) {
          output.write(buffer, 0, count);
        }

        input.close();
        output.close();
      }

      location.setExecutable(isDirectory, false);
      location.setWritable(false, false);
      location.setReadable(true, false);
    } catch (IOException exception) {
      Log.e(LOG_TAG, "directory refresh error: " + exception.getMessage());
    }
  }

  private static void extractAssets () {
    AssetManager assets = currentContext.getAssets();

    String name = "liblouis";
    String oldName = name + ".old";
    String newName = name + ".new";

    File location = new File(dataDirectory, name);
    File oldLocation = new File(dataDirectory, oldName);
    File newLocation = new File(dataDirectory, newName);

    removeFile(oldLocation);
    removeFile(newLocation);
    extractAssets(assets, name, newLocation);

    synchronized (NATIVE_LOCK) {
      location.renameTo(oldLocation);
      newLocation.renameTo(location);

      Log.d(LOG_TAG, "assets updated");
      releaseMemory();
    }

    removeFile(oldLocation);
  }

  private static void updatePackageData () {
    final SharedPreferences prefs = getSharedPreferences();
    final File file = new File(currentContext.getPackageCodePath());

    final String prefKey_size = "package-size";
    final long oldSize = prefs.getLong(prefKey_size, -1);
    final long newSize = file.length();

    final String prefKey_time = "package-time";
    final long oldTime = prefs.getLong(prefKey_time, -1);
    final long newTime = file.lastModified();

    if ((newSize != oldSize) || (newTime != oldTime)) {
      Log.d(LOG_TAG, "package size: " + oldSize + " -> " + newSize);
      Log.d(LOG_TAG, "package time: " + oldTime + " -> " + newTime);

      new Thread() {
        @Override
        public void run () {
          Log.d(LOG_TAG, "begin extracting assets");
          extractAssets();
          Log.d(LOG_TAG, "end extracting assets");

          SharedPreferences.Editor editor = prefs.edit();
          editor.putLong(prefKey_size, newSize);
          editor.putLong(prefKey_time, newTime);
          editor.commit();
        }
      }.start();
    }
  }

  public static void initialize (Context context) {
    synchronized (INITIALIZATION_LOCK) {
      if (currentContext != null) {
        throw new IllegalStateException("already initialized");
      }

      currentContext = context;
      dataDirectory = context.getDir(LIBRARY_NAME, Context.MODE_WORLD_READABLE);

      setDataPath(dataDirectory.getAbsolutePath());
    }

    updatePackageData();
  }

  public final static boolean compileTranslationTable (File table) {
    synchronized (NATIVE_LOCK) {
      return compileTranslationTable(table.getAbsolutePath());
    }
  }

  public final static boolean compileTranslationTable (TranslationTable table) {
    return compileTranslationTable(table.getFile());
  }

  public static BrailleTranslation getBrailleTranslation (
    TranslationTable table, CharSequence text,
    int brailleLength, int cursorOffset
  ) {
    return new BrailleTranslation(table, text, brailleLength, cursorOffset);
  }

  public static TextTranslation getTextTranslation (
    TranslationTable table, CharSequence braille,
    int textLength, int cursorOffset
  ) {
    return new TextTranslation(table, braille, textLength, cursorOffset);
  }

  private Louis () {
  }
}

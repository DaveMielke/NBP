package org.liblouis;

import android.util.Log;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;

public final class Louis {
  private final static String LOG_TAG = Louis.class.getName();

  public final static String ASSETS_FOLDER = "liblouis";

  public final static String toAssetsPath (String asset) {
    return ASSETS_FOLDER + File.separatorChar + asset;
  }

  private final static String LIBRARY_NAME = "louis";
  private final static String LIBRARY_VERSION;
  public native static String getVersion ();

  static {
    System.loadLibrary(LIBRARY_NAME);

    LIBRARY_VERSION = getVersion();
    Log.i(LOG_TAG, "liblouis version: " + LIBRARY_VERSION);
  }

  public static Object NATIVE_LOCK = new Object();
  public native static void releaseMemory ();
  public native static String getDataPath ();
  public native static void setDataPath (String path);
  private native static boolean compileTable (String tableList);
  private native static void setLogLevel (char character);

  public enum LogLevel {
    ALL  ('A'),
    DEBUG('D'),
    INFO ('I'),
    WARN ('W'),
    ERROR('E'),
    FATAL('F'),
    OFF  ('O'),
    ; // end of enumeration

    private final char levelCharacter;

    LogLevel (char character) {
      levelCharacter = character;
    }

    public final char getCharacter () {
      return levelCharacter;
    }
  }

  public static void setLogLevel (LogLevel level) {
    setLogLevel(level.getCharacter());
  }

  private final static Object INITIALIZATION_LOCK = new Object();
  private static Context applicationContext = null;
  private static AssetsExtractor assetsExtractor = null;
  private static File dataDirectory = null;

  private static void requireInitialization () {
    synchronized (INITIALIZATION_LOCK) {
      if (applicationContext == null) {
        throw new IllegalStateException("not initialized yet");
      }
    }
  }

  public static Context getContext () {
    requireInitialization();
    return applicationContext;
  }

  public static File getDataDirectory () {
    requireInitialization();
    return dataDirectory;
  }

  private static SharedPreferences getSharedPreferences () {
    return PreferenceManager.getDefaultSharedPreferences(applicationContext);
  }

  private static void updatePackageData (final NewInternalTablesListener newInternalTablesListener) {
    final SharedPreferences prefs = getSharedPreferences();
    final File file = new File(applicationContext.getPackageCodePath());

    final String prefKey_size = "package-size";
    final long oldSize = prefs.getLong(prefKey_size, -1);
    final long newSize = file.length();

    final String prefKey_time = "package-time";
    final long oldTime = prefs.getLong(prefKey_time, -1);
    final long newTime = file.lastModified();

    if ((newSize != oldSize) || (newTime != oldTime)) {
      Log.d(LOG_TAG, "package size: " + oldSize + " -> " + newSize);
      Log.d(LOG_TAG, "package time: " + oldTime + " -> " + newTime);

      AssetsExtractor.Listener listener =
        new AssetsExtractor.Listener() {
          @Override
          public void onFinished () {
            Log.i(LOG_TAG, "liblouis tables updated");
            releaseMemory();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(prefKey_size, newSize);
            editor.putLong(prefKey_time, newTime);
            editor.commit();

            if (newInternalTablesListener != null) {
              newInternalTablesListener.newTables();
            }
          }
        };

      assetsExtractor.extractAssets(listener, ASSETS_FOLDER);
    }
  }

  public static void initialize (Context context, NewInternalTablesListener newInternalTablesListener) {
    context = context.getApplicationContext();

    synchronized (INITIALIZATION_LOCK) {
      if (applicationContext == null) {
        applicationContext = context;
        assetsExtractor = new AssetsExtractor(context);
        dataDirectory = assetsExtractor.getDirectory();
        setDataPath(dataDirectory.getAbsolutePath());
        updatePackageData(newInternalTablesListener);
      } else if (context != applicationContext) {
        throw new IllegalArgumentException("different application context");
      } else if (newInternalTablesListener != null) {
        newInternalTablesListener.newTables();
      }
    }
  }

  public static void initialize (Context context) {
    initialize(context, null);
  }

  public final static boolean compile (String tableList) {
    synchronized (NATIVE_LOCK) {
      return compileTable(tableList);
    }
  }

  public final static boolean compile (File file) {
    return compile(file.getAbsolutePath());
  }

  public final static boolean compile (InternalTable table) {
    return compile(table.getList());
  }

  public final static boolean compile (InternalTranslator translator) {
    InternalTable forwardTable = translator.getForwardTable();
    if (!compile(forwardTable)) return false;

    InternalTable backwardTable = translator.getBackwardTable();
    if (backwardTable == forwardTable) return true;
    return compile(backwardTable);
  }

  public static BrailleTranslation getBrailleTranslation (
    Translator translator, CharSequence text
  ) {
    return new TranslationBuilder()
              .setTranslator(translator)
              .setInputCharacters(text)
              .setOutputLength((text.length() * 2) + 0X100)
              .setAllowLongerOutput(true)
              .newBrailleTranslation();
  }

  public static TextTranslation getTextTranslation (
    Translator translator, CharSequence braille
  ) {
    return new TranslationBuilder()
              .setTranslator(translator)
              .setInputCharacters(braille)
              .setOutputLength((braille.length() * 3) + 0X100)
              .setAllowLongerOutput(true)
              .newTextTranslation();
  }

  private Louis () {
  }
}

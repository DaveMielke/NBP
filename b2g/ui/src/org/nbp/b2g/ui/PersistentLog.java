package org.nbp.b2g.ui;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.Writer;

import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import android.util.Log;

public abstract class PersistentLog {
  private final static String LOG_TAG = PersistentLog.class.getName();

  private PersistentLog () {
  }

  private final static String TIME_FORMAT = "yyyy-MM-dd@HH:mm:ss.SSS";
  private final static String TIME_ZONE = "UTC";
  private final static SimpleDateFormat dateFormatter = new SimpleDateFormat(TIME_FORMAT);

  static {
    dateFormatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
  }

  private final static String getTime (long time) {
    return dateFormatter.format(time);
  }

  private final static String getTime () {
    return getTime(System.currentTimeMillis());
  }

  private final static Object LOG_LOCK = new Object();
  private static File logFile = null;
  private static Writer logWriter = null;

  private final static File getFile () {
    synchronized (LOG_LOCK) {
      if (logFile == null) {
        logFile = new File("/sdcard/Documents/B2G.log");
      }

      return logFile;
    }
  }

  private final static String getPath () {
    return getFile().getAbsolutePath();
  }

  private final static void logProblem (Exception exception, String action) {
    Log.w(LOG_TAG,
      String.format(
        "persistent log %s error: %s: %s",
        action, getPath(), exception.getMessage()
      )
    );
  }

  private final static boolean openLog () {
    synchronized (LOG_LOCK) {
      if (logWriter != null) return true;

      try {
        logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFile())));
        return true;
      } catch (IOException exception) {
        logProblem(exception, "open");
      }
    }

    return false;
  }

  public final static boolean write (String format, Object... items) {
    synchronized (LOG_LOCK) {
      if (openLog()) {
        try {
          logWriter.write(getTime());
          logWriter.write(' ');
          logWriter.write(String.format(format, items));
          logWriter.write('\n');
          logWriter.flush();
          return true;
        } catch (IOException exception) {
          logProblem(exception, "write");
        }
      }
    }

    return false;
  }
}

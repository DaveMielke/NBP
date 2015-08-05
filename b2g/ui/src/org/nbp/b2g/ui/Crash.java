package org.nbp.b2g.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import android.util.Log;

public abstract class Crash {
  private final static String LOG_TAG = Crash.class.getName();

  private static void logCrash (Throwable problem, String component, String data) {
    String message = problem.getMessage();
    StringBuilder sb = new StringBuilder();

    sb.append(component);
    sb.append(" crashed");

    if (data != null) {
      sb.append(": ");
      sb.append(data);
    }

    if (message != null) {
      sb.append(": ");
      sb.append(message);
    }

    Log.w(LOG_TAG, sb.toString(), problem);
  }

  private static File makeBacktraceFile (Throwable problem, String component, String data) {
    boolean success = false;

    File directory = ApplicationContext.getObjectDirectory(Crash.class);
    if (directory == null) return null;

    File file = new File(directory, "Java.log");
    if (file == null) return null;

    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

      try {
        StringBuilder sb = new StringBuilder();
        sb.append(component);

        if (data != null) {
          sb.append(": ");
          sb.append(data);
        }

        writer.write(sb.toString());
        writer.newLine();
        writer.newLine();

        {
          Throwable cause = problem;

          while (cause != null) {
            sb.setLength(0);
            if (cause != problem) sb.append("caused by: ");
            sb.append(cause.getClass().getName());

            {
              String message = cause.getMessage();

              if (message != null) {
                sb.append(": ");
                sb.append(message);
              }
            }

            writer.write(sb.toString());
            writer.newLine();

            for (StackTraceElement element : cause.getStackTrace()) {
              sb.setLength(0);
              sb.append("\tat ");
              sb.append(element.toString());
              writer.write(sb.toString());
              writer.newLine();
            }

            cause = cause.getCause();
          }
        }

        success = true;
      } finally {
        writer.close();
      }
    } catch (IOException exception) {
      Log.w(LOG_TAG, "backtrace file creation error", exception);
      success = false;
    }

    if (!success) return null;
    file.setReadable(true, false);
    return file;
  }

  private static void reportCrash (Throwable problem, String component, String data) {
    String[] recipients = ApplicationContext.getStringArray(R.array.recipients_crash);

    if (recipients.length > 0) {
      OutgoingMessage report = new OutgoingMessage();

      for (String recipient : recipients) {
        report.addPrimaryRecipient(recipient);
      }

      report.setSubject("Java backtrace sent by user");

      report.addBodyLine(R.string.email_to_the_user);
      report.addBodyLine();
      report.addBodyLine(R.string.email_sending_java_backtrace);
      report.addBodyLine(R.string.email_no_sensitive_data);

      {
        File file = makeBacktraceFile(problem, component, data);
        if (file != null) report.addAttachment(file);
      }

      report.send();
    }
  }

  public static void handleCrash (Throwable problem, String component, String data) {
    ApplicationUtilities.alert();
    logCrash(problem, component, data);

    if (ApplicationSettings.DEVELOPER_ENABLED) {
      reportCrash(problem, component, data);
    }
  }

  public static void handleCrash (Throwable problem, String component) {
    handleCrash(problem, component, null);
  }

  private Crash () {
  }
}

package org.nbp.b2g.ui;

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

  private static void reportCrash (Throwable problem, String component, String data) {
    String[] recipients = ApplicationContext.getStringArray(R.array.recipients_crash);

    if (recipients.length > 0) {
      OutgoingMessage report = new OutgoingMessage();

      for (String recipient : recipients) {
        report.addPrimaryRecipient(recipient);
      }

      {
        StringBuilder sb = new StringBuilder();
        sb.append(component);
        sb.append(" crash: ");
        sb.append(Crash.class.getPackage().getName());
        report.setSubject(sb.toString());
Log.w(LOG_TAG, "subject: " + sb.toString());
      }

      {
        StringBuilder sb = new StringBuilder();
        Throwable cause = problem;

        sb.append(component);
        if (data != null) {
          sb.append(": ");
          sb.append(data);
        }
        report.addBodyLine(sb.toString());

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

          report.addBodyLine(sb.toString());

          for (StackTraceElement element : cause.getStackTrace()) {
            sb.setLength(0);
            sb.append("\tat ");
            sb.append(element.toString());
            report.addBodyLine(sb.toString());
          }

          cause = cause.getCause();
        }
      }

      report.send();
    }
  }

  public static void handleCrash (Throwable problem, String component, String data) {
    ApplicationUtilities.alert();
    logCrash(problem, component, data);
    reportCrash(problem, component, data);
  }

  public static void handleCrash (Throwable problem, String component) {
    handleCrash(problem, component, null);
  }

  private Crash () {
  }
}

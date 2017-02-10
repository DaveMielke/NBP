package org.nbp.b2g.ui;

import java.util.concurrent.Callable;

import java.io.File;
import java.io.Writer;
import java.io.IOException;

import org.nbp.common.FileMaker;
import org.nbp.common.AttachmentMaker;

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

  private static File makeBacktraceFile (final Throwable problem, final String component, final String data) {
    FileMaker fileMaker = new AttachmentMaker() {
      @Override
      protected boolean writeContent (Writer writer) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append(component);
        sb.append(" that crashed");

        if (data != null) {
          sb.append(": ");
          sb.append(data);
        }

        writer.write(sb.toString());
        writer.write('\n');
        writer.write('\n');

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
            writer.write('\n');

            for (StackTraceElement element : cause.getStackTrace()) {
              sb.setLength(0);
              sb.append("\tat ");
              sb.append(element.toString());
              writer.write(sb.toString());
              writer.write('\n');
            }

            cause = cause.getCause();
          }
        }

        return true;
      }
    };

    return fileMaker.makeFile("Java.log", Crash.class);
  }

  private static boolean reportCrash (Throwable problem, String component, String data) {
    DeveloperMessage message = new DeveloperMessage() {
      @Override
      protected final boolean containsSensitiveData () {
        return false;
      }

      @Override
      protected final String getSubject () {
        return "Java backtrace sent by user";
      }
    };

    if (message.isSendable()) {
      {
        File file = makeBacktraceFile(problem, component, data);
        if (file == null) return false;
        message.addAttachment(file);
      }

      message.addLine(R.string.email_sending_java_backtrace);
      if (!message.send()) return false;
    }

    return true;
  }

  private static void handleCrash (Throwable problem, String component, String data) {
    Devices.tone.get().alert();
    logCrash(problem, component, data);

    if (ApplicationSettings.DEVELOPER_ENABLED) {
      reportCrash(problem, component, data);
    }
  }

  public static <R> R runComponent (String name, String data, Callable<R> callable) {
    try {
      return callable.call();
    } catch (Exception exception) {
      handleCrash(exception, name, data);
      return null;
    }
  }

  public static void runComponent (String name, String data, final Runnable runnable) {
    runComponent(
      name, data,
      new Callable<Void>() {
        @Override
        public Void call () {
          runnable.run();
          return null;
        }
      }
    );
  }

  private Crash () {
  }
}

package org.nbp.common;

import android.util.Log;
import android.os.Build;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;

public abstract class CommonUtilities {
  protected CommonUtilities () {
  }

  private static ProblemReporter errorReporter = new ProblemReporter() {
    @Override
    public final void reportProblem (String tag, String message) {
      Log.e(tag, message);
    }
  };

  public static void setErrorReporter (ProblemReporter reporter) {
    errorReporter = reporter;
  }

  public static void reportError (String tag, String message) {
    errorReporter.reportProblem(tag, message);
  }

  public static void reportError (String tag, String format, Object... arguments) {
    reportError(tag, String.format(format, arguments));
  }

  public static void reportError (String tag, int message) {
    reportError(tag, CommonContext.getString(message));
  }

  private static ProblemReporter warningReporter = new ProblemReporter() {
    @Override
    public final void reportProblem (String tag, String message) {
      Log.w(tag, message);
    }
  };

  public static void setWarningReporter (ProblemReporter reporter) {
    warningReporter = reporter;
  }

  public static void reportWarning (String tag, String message) {
    warningReporter.reportProblem(tag, message);
  }

  public static void reportWarning (String tag, String format, Object... arguments) {
    reportWarning(tag, String.format(format, arguments));
  }

  public static void reportWarning (String tag, int message) {
    reportWarning(tag, CommonContext.getString(message));
  }

  public final static View findView (DialogInterface dialog, int identifier) {
    return ((Dialog)dialog).findViewById(identifier);
  }

  public static boolean haveAndroidSDK (int sdk) {
    return sdk <= Build.VERSION.SDK_INT;
  }
}

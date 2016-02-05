package org.nbp.common;

import android.util.Log;

public abstract class CommonUtilities {
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

  public static void reportWarning (String tag, int message) {
    reportWarning(tag, CommonContext.getString(message));
  }

  protected CommonUtilities () {
  }
}

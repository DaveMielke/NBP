package org.nbp.common;

import android.util.Log;
import android.os.Build;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;

public abstract class CommonUtilities {
  private final static String LOG_TAG = CommonUtilities.class.getName();

  protected CommonUtilities () {
  }

  public static boolean haveAndroidSDK (int sdk) {
    return Build.VERSION.SDK_INT >= sdk;
  }

  public final static boolean haveJellyBean =
    haveAndroidSDK(Build.VERSION_CODES.JELLY_BEAN);

  public final static boolean haveJellyBeanMR1 =
    haveAndroidSDK(Build.VERSION_CODES.JELLY_BEAN_MR1);

  public final static boolean haveJellyBeanMR2 =
    haveAndroidSDK(Build.VERSION_CODES.JELLY_BEAN_MR2);

  public final static boolean haveKitkat =
    haveAndroidSDK(Build.VERSION_CODES.KITKAT);

  public final static boolean haveLollipop =
    haveAndroidSDK(Build.VERSION_CODES.LOLLIPOP);

  public final static boolean haveLollipopMR1 =
    haveAndroidSDK(Build.VERSION_CODES.LOLLIPOP_MR1);

  public final static boolean haveMarshmallow =
    haveAndroidSDK(Build.VERSION_CODES.M);

  public final static boolean haveNougat =
    haveAndroidSDK(Build.VERSION_CODES.N);

  public final static boolean haveNougatMR1 =
    haveAndroidSDK(Build.VERSION_CODES.N_MR1);

  public final static boolean haveOreo =
    haveAndroidSDK(Build.VERSION_CODES.O);

  public final static boolean haveOreoMR1 =
    haveAndroidSDK(Build.VERSION_CODES.O_MR1);

  public final static boolean havePie =
    haveAndroidSDK(Build.VERSION_CODES.P);

  public final static void runUnsafeCode (Runnable runnable) {
    try {
      runnable.run();
    } catch (RuntimeException exception) {
      Log.e(LOG_TAG, "unexpected exception", exception);
    }
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
}

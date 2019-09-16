package org.nbp.common.dictionary;

import android.util.Log;

public abstract class DictionaryResponse implements ResponseHandler {
  private final static String LOG_TAG = DictionaryResponse.class.getName();

  protected static DictionaryConnection getConnection () {
    return DictionaryConnection.singleton();
  }

  protected DictionaryResponse (String... arguments) {
    getConnection().startCommand(this, arguments);
  }

  private boolean hasFinished = false;

  @Override
  public final void setFinished () {
    synchronized (this) {
      if (hasFinished) {
        throw new IllegalStateException("already finished");
      }

      hasFinished = true;
      notify();
    }
  }

  protected static void logProblem (String problem) {
    Log.w(LOG_TAG, problem);
  }

  protected static void logProblem (String format, Object... arguments) {
    logProblem(String.format(format, arguments));
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.SERVER_OFFLINE:
        logProblem("server is offline");
        return true;

      case ResponseCodes.SERVER_PROBLEM:
        logProblem("temporary server problem");
        return true;

      default:
        logProblem("unhandled response code: %d", code);
        return false;
    }
  }
}

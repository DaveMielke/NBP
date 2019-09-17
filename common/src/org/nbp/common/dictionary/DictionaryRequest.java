package org.nbp.common.dictionary;

import android.util.Log;

public abstract class DictionaryRequest implements RequestHandler {
  private final static String LOG_TAG = DictionaryRequest.class.getName();

  private final DictionaryConnection dictionaryConnection = DictionaryConnection.get();

  protected final DictionaryConnection getConnection () {
    return dictionaryConnection;
  }

  protected DictionaryRequest (String... arguments) {
    getConnection().startCommand(this, arguments);
  }

  private boolean isFinished = false;

  public final boolean hasFinished () {
    return isFinished;
  }

  protected void handleResult () {
  }

  @Override
  public final void setFinished () {
    synchronized (this) {
      if (hasFinished()) {
        throw new IllegalStateException("already finished");
      }

      isFinished = true;
      notify();
      handleResult();
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

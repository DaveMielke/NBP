package org.nbp.common.dictionary;

import android.util.Log;

public abstract class MatchesRequest extends CommandRequest implements MatchesHandler {
  private final static String LOG_TAG = MatchesRequest.class.getName();

  private final MatchList savedMatches = new MatchList();

  protected MatchesRequest (String... arguments) {
    super(arguments);
  }

  public final MatchList getMatches () {
    return savedMatches;
  }

  @Override
  public void handleMatches (MatchList matches) {
  }

  @Override
  protected final void handleResult () {
    handleMatches(getMatches());
  }

  protected final void saveMatches (DictionaryOperands operands) {
    for (String match : getTextAsList()) {
      try {
        DictionaryOperands parameters = new DictionaryOperands(match);

        if (parameters.isEmpty()) throw new OperandException("missing database name");
        String name = parameters.removeFirst();

        if (parameters.isEmpty()) throw new OperandException("missing matched word");
        String word = parameters.removeFirst();

        savedMatches.add(word, name);
      } catch (OperandException exception) {
        Log.w(LOG_TAG, exception.getMessage());
      }
    }
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.UNKNOWN_DATABASE:
        logProblem("unknown database");
        return true;

      case ResponseCodes.UNKNOWN_STRATEGY:
        logProblem("unknown strategy");
        return true;

      case ResponseCodes.NO_MATCH:
        return true;

      case ResponseCodes.BEGIN_MATCH_LIST:
        saveMatches(operands);
        return false;

      default:
        return super.handleResponse(code, operands);
    }
  }
}

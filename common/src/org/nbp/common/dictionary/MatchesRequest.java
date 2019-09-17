package org.nbp.common.dictionary;

import android.util.Log;

public abstract class MatchesRequest extends CommandRequest implements MatchesHandler {
  private final static String LOG_TAG = MatchesRequest.class.getName();

  private final MatchList matches = new MatchList();

  protected MatchesRequest (String... arguments) {
    super(arguments);
  }

  public final MatchList getMatches () {
    return matches;
  }

  @Override
  public void handleMatches (MatchList matches) {
  }

  @Override
  protected final void handleResult () {
    handleMatches(getMatches());
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.NO_MATCH:
        return true;

      case ResponseCodes.BEGIN_MATCH_LIST: {
        for (String match : getTextAsList()) {
          try {
            DictionaryOperands parameters = new DictionaryOperands(match);

            if (parameters.isEmpty()) throw new OperandException("missing database name");
            String name = parameters.removeFirst();

            if (parameters.isEmpty()) throw new OperandException("missing matched word");
            String word = parameters.removeFirst();

            matches.add(word, name);
          } catch (OperandException exception) {
            Log.w(LOG_TAG, exception.getMessage());
          }
        }

        return false;
      }

      default:
        return super.handleResponse(code, operands);
    }
  }
}

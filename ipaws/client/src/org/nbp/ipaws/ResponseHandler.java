package org.nbp.ipaws;

import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

public abstract class ResponseHandler {
  public abstract void handleResponse (String response);

  private final static Map<String, Pattern> patterns =
               new HashMap<String, Pattern>();

  protected final static Pattern getPattern (String string) {
    synchronized (patterns) {
      Pattern pattern = patterns.get(string);
      if (pattern != null) return pattern;

      pattern = Pattern.compile(string);
      patterns.put(string, pattern);
      return pattern;
    }
  }

  protected final String[] getOperands (String string, int count, String delimiter) {
    return getPattern(delimiter).split(string, count);
  }

  protected final String[] getOperands (String string, int count) {
    return getOperands(string.trim(), count, "\\s+");
  }

  protected final String[] getOperands (String string) {
    return getOperands(string, 0);
  }

  protected final String[] getOperands (String string, String delimiter) {
    return getOperands(string, 0, delimiter);
  }
}

package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.util.Log;

public class FindEndpoint extends PromptEndpoint {
  private final static String LOG_TAG = FindEndpoint.class.getName();

  private Pattern occurrencePattern = null;

  private abstract class MatchFinder {
    public abstract boolean findMatch (Matcher matcher, int start, int end);
  }

  private final boolean findOccurrence (MatchFinder matchFinder) {
    if (occurrencePattern != null) {
      Endpoint endpoint = Endpoints.host.get();
      boolean found = false;

      synchronized (endpoint) {
        int start = endpoint.getBrailleStart();
        String text = endpoint.getText().toString();
        Matcher matcher = occurrencePattern.matcher(text);

        if (matchFinder.findMatch(matcher, start, text.length())) {
          endpoint.setLineIndent(endpoint.setLine(matcher.start(1)));
          found = true;
        }
      }

      if (found) return endpoint.write();
    }

    return false;
  }

  public final boolean findNextOccurrence () {
    MatchFinder matchFinder = new MatchFinder() {
      @Override
      public boolean findMatch (Matcher matcher, int start, int end) {
        while (++start < end) {
          matcher.region(start, end);
          if (matcher.lookingAt()) return true;
          if (matcher.hitEnd()) break;
        }

        return false;
      }
    };

    return findOccurrence(matchFinder);
  }

  public final boolean findPreviousOccurrence () {
    MatchFinder matchFinder = new MatchFinder() {
      @Override
      public boolean findMatch (Matcher matcher, int start, int end) {
        int original = start;

        while (--start >= 0) {
          matcher.region(start, end);

          if (matcher.lookingAt()) {
            if (matcher.start(1) != original) {
              return true;
            }
          }
        }

        return false;
      }
    };

    return findOccurrence(matchFinder);
  }

  private final void setOccurrencePattern (String response) {
    StringBuilder sb = new StringBuilder();

    String[] words = response.split("\\s+", -1);
    String separator = null;

    int from = 0;
    int to = words.length;

    if (to > from) {
      if (!words[--to].isEmpty()) {
        to += 1;
      }
    }

    if (from < to) {
      if (words[from].isEmpty()) {
        sb.append("(?:\\A|\\s)");
        from += 1;
      }
    }

    sb.append("(?i)");
    sb.append('(');

    while (from < to) {
      String word = words[from++];
      if (word.isEmpty()) continue;

      if (separator == null) {
        separator = "\\s+";
      } else {
        sb.append(separator);
      }

      sb.append("\\Q");
      sb.append(word);
      sb.append("\\E");
    }

    if (to < words.length) sb.append("(?:\\s|\\z)");
    sb.append(')');

    occurrencePattern = Pattern.compile(sb.toString());
  }

  @Override
  protected final boolean handleResponse (String response) {
    setOccurrencePattern(response);
    return findNextOccurrence();
  }

  public FindEndpoint () {
    super(R.string.prompt_find);
  }
}

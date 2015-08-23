package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.util.Log;

public class FindEndpoint extends PromptEndpoint {
  private final static String LOG_TAG = FindEndpoint.class.getName();

  private final Pattern makePattern (String response) {
    StringBuilder sb = new StringBuilder();

    String[] words = response.split("\\s+");
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
        sb.append("\\s");
        from += 1;
      }
    }

    sb.append("(?i)");
    sb.append('(');

    while (from < to) {
      String word = words[from++];

      if (separator != null) {
        sb.append(separator);
      } else {
        separator = "\\s+";
      }

      if (!word.isEmpty()) {
        sb.append("\\Q");
        sb.append(word);
        sb.append("\\E");
      }
    }

    if (to < words.length) sb.append("\\s");
    sb.append(')');

    return Pattern.compile(sb.toString());
  }

  private abstract class MatchFinder {
    public abstract boolean findMatch (Matcher matcher, int start, int end);
  }

  private final boolean findOccurrence (String response, MatchFinder matchFinder) {
    if (response.length() > 0) {
      Endpoint endpoint = Endpoints.host.get();
      boolean found = false;

      synchronized (endpoint) {
        int start = endpoint.getBrailleStart();
        String text = endpoint.getText().toString();
        Matcher matcher = makePattern(response).matcher(text);

        if (matchFinder.findMatch(matcher, start, text.length())) {
          endpoint.setLineIndent(endpoint.setLine(matcher.start(1)));
          found = true;
        }
      }

      if (found) return endpoint.write();
    }

    return false;
  }

  private final boolean findNextOccurrence (String response) {
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

    return findOccurrence(response, matchFinder);
  }

  private final boolean findPreviousOccurrence (String response) {
    MatchFinder matchFinder = new MatchFinder() {
      @Override
      public boolean findMatch (Matcher matcher, int start, int end) {
        while (--start >= 0) {
          matcher.region(start, end);
          if (matcher.lookingAt()) return true;
        }

        return false;
      }
    };

    return findOccurrence(response, matchFinder);
  }

  public final boolean findNextOccurrence () {
    return findNextOccurrence(getResponse());
  }

  public final boolean findPreviousOccurrence () {
    return findPreviousOccurrence(getResponse());
  }

  @Override
  protected final boolean handleResponse (String response) {
    return findNextOccurrence(response);
  }

  public FindEndpoint () {
    super(R.string.prompt_find);
  }
}

package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.util.Log;

public class FindEndpoint extends PromptEndpoint {
  private final static String LOG_TAG = FindEndpoint.class.getName();

  private Pattern searchPattern = null;

  private abstract class Searcher {
    public abstract boolean search (Matcher matcher, int start, int end);
  }

  private final boolean findOccurrence (Searcher searcher) {
    if (searchPattern != null) {
      Endpoint endpoint = Endpoints.host.get();
      boolean found = false;

      synchronized (endpoint) {
        int start = endpoint.getBrailleStart();
        String text = endpoint.getText().toString();
        Matcher matcher = searchPattern.matcher(text);
        matcher.useAnchoringBounds(false);

        if (searcher.search(matcher, start, text.length())) {
          endpoint.setLineIndent(endpoint.setLine(matcher.start(1)));
          found = true;
        }
      }

      if (found) return endpoint.write();
    }

    return false;
  }

  private final Searcher forwardSearch = new Searcher() {
    @Override
    public final boolean search (Matcher matcher, int start, int end) {
      while (++start < end) {
        matcher.region(start, end);
        if (matcher.lookingAt()) return true;
        if (matcher.hitEnd()) break;
      }

      return false;
    }
  };

  public final boolean findNextOccurrence () {
    return findOccurrence(forwardSearch);
  }

  private final Searcher backwardSearch = new Searcher() {
    @Override
    public final boolean search (Matcher matcher, int start, int end) {
      int original = start;

      while (--start >= 0) {
        matcher.region(start, end);

        if (matcher.lookingAt()) {
          if (matcher.start(1) < original) {
            return true;
          }
        }
      }

      return false;
    }
  };

  public final boolean findPreviousOccurrence () {
    return findOccurrence(backwardSearch);
  }

  private final void setSearchPattern (String string) {
    StringBuilder sb = new StringBuilder();

    String[] words = string.split("\\s+", -1);
    String separator = null;

    int from = 0;
    int to = words.length;

    if (from < to) {
      if (words[from].isEmpty()) {
        sb.append("(?:^|\\s)\\s*+");
        from += 1;
      }
    }

    if (to > from) {
      if (!words[--to].isEmpty()) {
        to += 1;
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

    if (to < words.length) sb.append("(?:\\s|$)");
    sb.append(')');
    searchPattern = Pattern.compile(sb.toString());

    if (ApplicationSettings.LOG_ACTIONS) {
      Log.v(LOG_TAG, "search pattern: " + searchPattern.pattern());
    }
  }

  @Override
  protected final boolean handleResponse (String response) {
    setSearchPattern(response);
    return findNextOccurrence();
  }

  public FindEndpoint () {
    super(R.string.prompt_find);
  }
}

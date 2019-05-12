package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.util.Log;

public class FindEndpoint extends PromptEndpoint {
  private final static String LOG_TAG = FindEndpoint.class.getName();

  private abstract class Searcher {
    public abstract boolean search (Matcher matcher, int start, int end);
  }

  private final Searcher forwardSearcher = new Searcher() {
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

  private final Searcher backwardSearcher = new Searcher() {
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

  private Searcher currentSearcher = forwardSearcher;
  private Pattern searchPattern = null;

  private final boolean findOccurrence (Endpoint endpoint) {
    if (searchPattern != null) {
      boolean found = false;

      synchronized (endpoint) {
        int start = endpoint.getBrailleStart();
        String text = endpoint.getText().toString();
        Matcher matcher = searchPattern.matcher(text);
        matcher.useAnchoringBounds(false);

        if (currentSearcher.search(matcher, start, text.length())) {
          endpoint.setLineIndent(endpoint.setLine(matcher.start(1)));
          found = true;
        }
      }

      if (found) return endpoint.write();
    }

    return false;
  }

  public final boolean findNextOccurrence (Endpoint endpoint) {
    currentSearcher = forwardSearcher;
    return findOccurrence(endpoint);
  }

  public final boolean findPreviousOccurrence (Endpoint endpoint) {
    currentSearcher = backwardSearcher;
    return findOccurrence(endpoint);
  }

  public final void setDirection (boolean backward) {
    currentSearcher = backward? backwardSearcher: forwardSearcher;
  }

  private final void setSearchPattern (String string) {
    StringBuilder sb = new StringBuilder();
    sb.append("(?i)");

    String[] words = string.split("\\s+", -1);
    int from = 0;
    int to = words.length;

    if (from < to) {
      if (words[from].isEmpty()) {
        sb.append("(?:^|\\s)");
        from += 1;
      }
    }

    if (to > from) {
      if (!words[--to].isEmpty()) {
        to += 1;
      }
    }

    String separator = null;
    sb.append('(');

    while (from < to) {
      String word = words[from++];
      int length = word.length();
      if (length == 0) continue;

      if (separator == null) {
        separator = "\\s+";
      } else {
        sb.append(separator);
      }

      for (int index=0; index<length; index+=1) {
        char character = word.charAt(index);
        if (!Character.isLetterOrDigit(character)) sb.append('\\');
        sb.append(character);
      }
    }

    sb.append(')');
    if (to < words.length) sb.append("(?:\\s|$)");
    searchPattern = Pattern.compile(sb.toString());

    if (ApplicationSettings.LOG_ACTIONS) {
      Log.v(LOG_TAG, "search pattern: " + searchPattern.pattern());
    }
  }

  @Override
  protected final boolean handleResponse (String response) {
    setSearchPattern(response);
    return findOccurrence(Endpoints.getPreviousEndpoint());
  }

  public FindEndpoint () {
    super(R.string.prompt_find);
  }
}

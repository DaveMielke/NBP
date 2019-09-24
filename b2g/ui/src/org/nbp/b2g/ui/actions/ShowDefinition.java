package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import java.util.ArrayList;

import org.nbp.common.dictionary.*;

public class ShowDefinition extends CursorKeyAction {
  private static void showText (CharSequence text) {
    Endpoints.pushPopupEndpoint(text);
  }

  private static void showNoDefinitions (String word) {
    StringBuilder text = new StringBuilder()
      .append(getString(R.string.ShowDefinition_no_definitions))
      .append(": ")
      .append(word)
      ;

    showText(text);
  }

  private final void showDefinition (DefinitionEntry definition) {
    String text = definition.getDefinitionText();
    int end = text.length();

    while (end > 0) {
      if (!Character.isWhitespace(text.charAt(--end))) {
        end += 1;
        break;
      }
    }

    showText(text.substring(0, end));
  }

  private final void listDefinitions (final DefinitionList definitions) {
    synchronized (this) {
      StringBuilder text = new StringBuilder();
      text.append(getString(R.string.ShowDefinition_select_definition));

      {
        int index = 0;

        for (DefinitionEntry definition : definitions) {
          text.append('\n');
          text.append(++index);

          text.append(": ");
          text.append(definition.getMatchedWord());

          text.append(": ");
          text.append(definition.getDatabaseDescription());
        }
      }

      Endpoints.pushPopupEndpoint(
        text, 1,
        new PopupClickHandler() {
          @Override
          public boolean handleClick (int index) {
            showDefinition(definitions.get(index));
            return true;
          }
        }
      );
    }
  }

  private final void showDefinitions (final DefinitionList definitions, final String word) {
    synchronized (this) {
      int count = definitions.size();

      if (!ApplicationSettings.MULTIPLE_DEFINITIONS) {
        if (count > 1) {
          count = 1;
        }
      }

      if (count == 0) {
        if (ApplicationSettings.SUGGEST_WORDS) {
          requestMatches(word);
        } else {
          showNoDefinitions(word);
        }
      } else if (count == 1) {
        showDefinition(definitions.get(0));
      } else {
        listDefinitions(definitions);
      }
    }
  }

  private final void requestDefinitions (final String word) {
    synchronized (this) {
      new DefineCommand(word, ApplicationSettings.DICTIONARY_DATABASE) {
        @Override
        public void handleDefinitions (DefinitionList definitions) {
          showDefinitions(definitions, word);
        }
      };
    }
  }

  private final void listMatches (MatchList matches) {
    synchronized (this) {
      final ArrayList<String> words = new ArrayList<String>();

      for (MatchEntry match : matches) {
        String word = match.getMatchedWord();
        if (words.contains(word)) continue;
        words.add(word);
      }

      StringBuilder text = new StringBuilder();
      text.append(getString(R.string.ShowDefinition_select_match));

      {
        int index = 0;

        for (String word : words) {
          text.append('\n');
          text.append(++index);

          text.append(": ");
          text.append(word);
        }
      }

      Endpoints.pushPopupEndpoint(
        text, 1,
        new PopupClickHandler() {
          @Override
          public boolean handleClick (int index) {
            requestDefinitions(words.get(index));
            return true;
          }
        }
      );
    }
  }

  private final void showMatches (final MatchList matches, String word) {
    synchronized (this) {
      if (matches.isEmpty()) {
        showNoDefinitions(word);
      } else {
        listMatches(matches);
      }
    }
  }

  private final void requestMatches (final String word) {
    synchronized (this) {
      new MatchCommand(word, ApplicationSettings.DICTIONARY_STRATEGY, ApplicationSettings.DICTIONARY_DATABASE) {
        @Override
        public void handleMatches (MatchList matches) {
          showMatches(matches, word);
        }
      };
    }
  }

  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    int from = offset;
    CharSequence text = endpoint.getLineText();
    if (Character.isWhitespace(text.charAt(from))) return false;

    while (from > 0) {
      if (Character.isWhitespace(text.charAt(--from))) {
        from += 1;
        break;
      }
    }

    int length = text.length();
    int to = offset;

    while (++to < length) {
      if (Character.isWhitespace(text.charAt(to))) break;
    }

    requestDefinitions(text.subSequence(from, to).toString());
    return true;
  }

  public ShowDefinition (Endpoint endpoint) {
    super(endpoint, false);
  }
}

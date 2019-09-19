package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

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

  private static void listMatches (MatchList matches) {
    StringBuilder text = new StringBuilder();
    text.append(getString(R.string.ShowDefinition_select_suggestion));

    {
      int index = 0;

      for (MatchEntry match : matches) {
        text.append('\n');
        text.append(++index);

        text.append(": ");
        text.append(match.getMatchedWord());
      }
    }

    Endpoints.pushPopupEndpoint(
      text, 1,
      new PopupClickHandler() {
        @Override
        public boolean handleClick (int index) {
          return true;
        }
      }
    );
  }

  private static void showMatches (final MatchList matches, String word) {
    if (matches.isEmpty()) {
      showNoDefinitions(word);
      return;
    }

    listMatches(matches);
  }

  private static void requestMatches (final String word) {
    new MatchCommand(word, ApplicationSettings.DICTIONARY_STRATEGY, ApplicationSettings.DICTIONARY_DATABASE) {
      @Override
      public void handleMatches (MatchList matches) {
        showMatches(matches, word);
      }
    };
  }

  private static void listDefinitions (final DefinitionList definitions) {
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
          showText(definitions.get(index).getDefinitionText());
          return true;
        }
      }
    );
  }

  private static void showDefinitions (final DefinitionList definitions, final String word) {
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

      return;
    }

    if (count == 1) {
      showText(definitions.get(0).getDefinitionText());
      return;
    }

    listDefinitions(definitions);
  }

  private static void requestDefinition (final String word) {
    new DefineCommand(word, ApplicationSettings.DICTIONARY_DATABASE) {
      @Override
      public void handleDefinitions (DefinitionList definitions) {
        showDefinitions(definitions, word);
      }
    };
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

    requestDefinition(text.subSequence(from, to).toString());
    return true;
  }

  public ShowDefinition (Endpoint endpoint) {
    super(endpoint, false);
  }
}

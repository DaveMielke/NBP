package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.dictionary.*;

public class ShowDefinition extends CursorKeyAction {
  private static void showText (CharSequence text) {
    Endpoints.pushPopupEndpoint(text);
  }

  private static void showNoDefinition (String word) {
    StringBuilder text = new StringBuilder()
      .append(getString(R.string.ShowDefinition_no_definition))
      .append(": ")
      .append(word)
      ;

    showText(text);
  }

  private static void showMatches (final MatchList matches, String word) {
    if (matches.isEmpty()) {
      showNoDefinition(word);
      return;
    }

    StringBuilder text = new StringBuilder();
    text.append(getString(R.string.ShowDefinition_select_suggestion));

    {
      int index = 0;

      for (MatchEntry match : matches) {
        text.append('\n');
        text.append(++index);

        text.append(": ");
        text.append(match.getMatchedWord());

        text.append(": ");
        text.append(match.getDatabaseName());
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

  private static void showDefinitions (final DefinitionList definitions, final String word) {
    int count = definitions.size();

    if (!ApplicationSettings.MULTIPLE_DEFINITIONS) {
      if (count > 1) {
        count = 1;
      }
    }

    if (count == 0) {
      if (ApplicationSettings.SUGGEST_WORDS) {
        new MatchCommand(word, ApplicationSettings.DICTIONARY_STRATEGY, ApplicationSettings.DICTIONARY_DATABASE) {
          @Override
          public void handleMatches (MatchList matches) {
            showMatches(matches, word);
          }
        };
      } else {
        showNoDefinition(word);
      }

      return;
    }

    if (count == 1) {
      showText(definitions.get(0).getDefinitionText());
      return;
    }

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

    final String word = text.subSequence(from, to).toString();
    final DictionaryDatabase database = ApplicationSettings.DICTIONARY_DATABASE;

    new DefineCommand(word, database) {
      @Override
      public void handleDefinitions (DefinitionList definitions) {
        showDefinitions(definitions, word);
      }
    };

    Dictionary.endSession();
    return true;
  }

  public ShowDefinition (Endpoint endpoint) {
    super(endpoint, false);
  }
}

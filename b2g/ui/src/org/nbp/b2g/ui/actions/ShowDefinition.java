package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.dictionary.*;

public class ShowDefinition extends CursorKeyAction {
  private static void showText (CharSequence text) {
    Endpoints.pushPopupEndpoint(text);
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
      public void handleDefinitions (final DefinitionList definitions) {
        int size = definitions.size();

        if (!ApplicationSettings.MULTIPLE_DEFINITIONS) {
          if (size > 1) {
            size = 1;
          }
        }

        if (size == 0) {
          StringBuilder text = new StringBuilder()
            .append(getString(R.string.ShowDefinition_no_definition))
            .append(": ")
            .append(word)
            ;

          showText(text);
          return;
        }

        if (size == 1) {
          showText(definitions.get(0).getDefinitionText());
          return;
        }

        StringBuilder text = new StringBuilder();
        text.append(getString(R.string.popup_select_definition));

        for (int index=0; index<size; index+=1) {
          DefinitionEntry definition = definitions.get(index);

          text.append('\n');
          text.append(index+1);

          text.append(": ");
          text.append(definition.getMatchedWord());

          text.append(": ");
          text.append(definition.getDatabaseDescription());
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
    };

    Dictionary.endSession();
    return true;
  }

  public ShowDefinition (Endpoint endpoint) {
    super(endpoint, false);
  }
}

package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.dictionary.*;
import org.nbp.b2g.ui.popup.PopupEndpoint;

public class ShowDefinition extends CursorKeyAction {
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

    new DefineCommand(text.subSequence(from, to).toString(), ApplicationSettings.DICTIONARY_DATABASE) {
      @Override
      protected void handleResult (final DefinitionList definitions) {
        int size = definitions.size();
        if (size == 0) return;

        if (size == 1) {
          Endpoints.setCurrentEndpoint(new PopupEndpoint().set(definitions.get(0).getDefinitionText()));
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

        Endpoints.setCurrentEndpoint(
          new PopupEndpoint().set(
            text, 1,
            new PopupClickHandler() {
              @Override
              public boolean handleClick (int index) {
                Endpoints.setCurrentEndpoint(new PopupEndpoint().set(definitions.get(index).getDefinitionText()));
                return true;
              }
            }
          )
        );
      }
    };

    return true;
  }

  public ShowDefinition (Endpoint endpoint) {
    super(endpoint, false);
  }
}

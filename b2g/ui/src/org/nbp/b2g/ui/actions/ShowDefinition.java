package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.dictionary.*;
import org.nbp.b2g.ui.popup.PopupEndpoint;

public class ShowDefinition extends CursorKeyAction {
  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    CharSequence text = endpoint.getLineText();

    int from = offset;
    if (endpoint.isWordBreak(from)) return false;

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

    new DefineCommand(text.subSequence(from, to).toString()) {
      @Override
      protected void handleResult (final DefinitionList definitions) {
        int size = definitions.size();
        String text = null;
        int first = 0;
        PopupClickHandler handler = null;

        if (size == 0) {
        } else if (size == 1) {
          text = definitions.get(0).getDefinitionText();
        } else {
          {
            StringBuilder sb = new StringBuilder();

            sb.append(getString(R.string.popup_select_definition));
            first = 1;

            for (int index=0; index<size; index+=1) {
              DefinitionEntry definition = definitions.get(index);

              sb.append('\n');
              sb.append(index+1);

              sb.append(": ");
              sb.append(definition.getMatchedWord());

              sb.append(": ");
              sb.append(definition.getDatabaseDescription());
            }

            text = sb.toString();
          }

          handler = new PopupClickHandler() {
            @Override
            public boolean handleClick (int index) {
              PopupEndpoint endpoint = new PopupEndpoint();
              endpoint.set(definitions.get(index).getDefinitionText(), 0, null);
              Endpoints.setCurrentEndpoint(endpoint);
              return true;
            }
          };
        }

        if (text != null) {
          PopupEndpoint endpoint = new PopupEndpoint();
          endpoint.set(text, first, handler);
          Endpoints.setCurrentEndpoint(endpoint);
        }
      }
    };

    return true;
  }

  public ShowDefinition (Endpoint endpoint) {
    super(endpoint, false);
  }
}

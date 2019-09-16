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
      protected void handleResult (DefinitionList definitions) {
        int size = definitions.size();
        String text = null;
        int first = 0;

        if (size == 0) {
        } else if (size == 1) {
          text = definitions.get(0).getDefinitionText();
        } else {
          StringBuilder sb = new StringBuilder();

          for (int index=0; index<size; index+=1) {
            DefinitionEntry definition = definitions.get(index);
            if (sb.length() > 0) sb.append('\n');
            sb.append(index+1);
            sb.append(": ");
            sb.append(definition.getMatchedWord());
            sb.append(": ");
            sb.append(definition.getDatabaseDescription());
          }

          text = sb.toString();
        }

        if (text != null) {
          PopupEndpoint endpoint = new PopupEndpoint();
          endpoint.set(text, first, null);
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

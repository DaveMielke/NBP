package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.text.style.SuggestionSpan;
import android.text.Spanned;

public class SpellingSuggest extends SpanAction {
  @Override
  public boolean performAction () {
    final Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        final Spanned text = toSpannedText(endpoint);

        if (text != null) {
          SuggestionSpan span = getSpan(
            SuggestionSpan.class, text,
            endpoint.getSelectionStart(),
            endpoint.getSelectionEnd()
          );

          if (span != null) {
            final String[] suggestions = span.getSuggestions();
            final int start = text.getSpanStart(span);
            final int end = text.getSpanEnd(span);

            StringBuilder message = new StringBuilder();
            message.append(getString(R.string.popup_select_suggestion));

            for (String suggestion : suggestions) {
              message.append('\n');
              message.append(suggestion);
            }

            return Endpoints.setPopupEndpoint(message.toString(), 1,
              new PopupClickHandler() {
                @Override
                public boolean handleClick (int index) {
                  String suggestion = suggestions[index];

                  synchronized (endpoint) {
                    return endpoint.replaceText(start, end, suggestion);
                  }
                }
              }
            );
          }
        }
      }
    }

    ApplicationUtilities.message(R.string.message_no_suggestions);
    return false;
  }

  public SpellingSuggest (Endpoint endpoint) {
    super(endpoint);
  }
}

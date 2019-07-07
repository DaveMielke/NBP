package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.text.style.SuggestionSpan;

public class SpellingNext extends SpanAction {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        Object span = findNextSpan(endpoint, SuggestionSpan.class);

        if (span != null) {
          return moveToSpan(endpoint, span);
        } else {
          ApplicationUtilities.message(R.string.message_not_found);
        }
      }
    }

    return false;
  }

  public SpellingNext (Endpoint endpoint) {
    super(endpoint);
  }
}

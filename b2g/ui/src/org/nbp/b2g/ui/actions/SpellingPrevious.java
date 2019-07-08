package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.text.style.SuggestionSpan;

public class SpellingPrevious extends SpanAction {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        int offset = findPreviousSpan(endpoint, SuggestionSpan.class);

        if (offset != NOT_FOUND) {
          return endpoint.setCursor(offset);
        } else {
          ApplicationUtilities.message(R.string.message_not_found);
        }
      }
    }

    return false;
  }

  public SpellingPrevious (Endpoint endpoint) {
    super(endpoint);
  }
}

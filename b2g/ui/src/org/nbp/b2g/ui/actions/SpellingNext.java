package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.text.style.SuggestionSpan;

public class SpellingNext extends SpanAction {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        int offset = findNextSpan(endpoint, SuggestionSpan.class);
        if (offset != NOT_FOUND) return endpoint.setCursor(offset);
      }
    }

    ApplicationUtilities.message(R.string.message_not_found);
    return false;
  }

  public SpellingNext (Endpoint endpoint) {
    super(endpoint);
  }
}

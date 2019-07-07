package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.text.style.SuggestionSpan;

public class SpellingNext extends SpanAction {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        if (moveToSpan(endpoint, findNextSpan(endpoint, SuggestionSpan.class))) {
          return true;
        }
      }
    }

    return false;
  }

  public SpellingNext (Endpoint endpoint) {
    super(endpoint);
  }
}

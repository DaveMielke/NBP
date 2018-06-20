package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class FindPrevious extends Action {
  @Override
  public boolean performAction () {
    return Endpoints.find.get().findPreviousOccurrence(getEndpoint());
  }

  public FindPrevious (Endpoint endpoint) {
    super(endpoint, false);
  }
}

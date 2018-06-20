package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class FindNext extends Action {
  @Override
  public boolean performAction () {
    return Endpoints.find.get().findNextOccurrence(getEndpoint());
  }

  public FindNext (Endpoint endpoint) {
    super(endpoint, false);
  }
}

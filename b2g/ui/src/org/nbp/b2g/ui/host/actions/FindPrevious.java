package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class FindPrevious extends Action {
  @Override
  public boolean performAction () {
    return Endpoints.find.get().findPreviousOccurrence();
  }

  public FindPrevious (Endpoint endpoint) {
    super(endpoint, false);
  }
}

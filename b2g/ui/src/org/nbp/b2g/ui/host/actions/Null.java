package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class Null extends Action {
  @Override
  public boolean performAction () {
    return true;
  }

  public Null (Endpoint endpoint) {
    super(endpoint, false);
  }
}

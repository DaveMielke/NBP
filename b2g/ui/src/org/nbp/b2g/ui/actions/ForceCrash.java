package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ForceCrash extends Action {
  @Override
  public boolean performAction () {
    Object object = null;
    String string = object.toString();
    return true;
  }

  public ForceCrash (Endpoint endpoint) {
    super(endpoint, true);
  }
}

package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class Editor extends ExternalActivityAction {
  @Override
  protected String getPackageName () {
    return "org.nbp.editor";
  }

  public Editor (Endpoint endpoint) {
    super(endpoint, false);
  }
}

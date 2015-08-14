package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class OneHandOn extends NextValueAction {
  public OneHandOn (Endpoint endpoint) {
    super(endpoint, Controls.getOneHandControl(), false);
  }
}

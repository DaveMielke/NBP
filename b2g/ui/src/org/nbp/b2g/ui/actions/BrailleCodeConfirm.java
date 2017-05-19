package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleCodeConfirm extends ConfirmValueAction {
  public BrailleCodeConfirm (Endpoint endpoint) {
    super(endpoint, Controls.brailleCode, false);
  }
}

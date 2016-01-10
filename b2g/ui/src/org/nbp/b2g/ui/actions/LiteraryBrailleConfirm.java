package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LiteraryBrailleConfirm extends ConfirmValueAction {
  public LiteraryBrailleConfirm (Endpoint endpoint) {
    super(endpoint, Controls.getLiteraryBrailleControl(), false);
  }
}

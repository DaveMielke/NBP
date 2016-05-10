package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class WordWrapOff extends PreviousValueAction {
  public WordWrapOff (Endpoint endpoint) {
    super(endpoint, Controls.getWordWrapControl(), false);
  }
}

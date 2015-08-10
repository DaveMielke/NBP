package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputModeBraille extends Action {
  @Override
  public boolean performAction () {
    return Controls.getInputModeControl().nextValue();
  }

  public InputModeBraille (Endpoint endpoint) {
    super(endpoint, false);
  }
}

package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DescribeActions extends Action {
  @Override
  public boolean performAction () {
    ActionChooser.chooseAction(getEndpoint().getKeyBindings().getRootKeyBindingMap());
    return true;
  }

  public DescribeActions (Endpoint endpoint) {
    super(endpoint, false);
  }
}

package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SwitchLauncher extends Action {
  @Override
  public boolean performAction () {
    return ApplicationContext.switchLauncher();
  }

  public SwitchLauncher (Endpoint endpoint) {
    super(endpoint, true);
  }
}

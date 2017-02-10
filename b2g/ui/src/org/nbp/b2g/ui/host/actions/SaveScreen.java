package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class SaveScreen extends Action {
  @Override
  public boolean performAction () {
    return new LogScreenLogger().logScreen();
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.SaveScreen_action_confirmation;
  }

  public SaveScreen (Endpoint endpoint) {
    super(endpoint, true);
  }
}

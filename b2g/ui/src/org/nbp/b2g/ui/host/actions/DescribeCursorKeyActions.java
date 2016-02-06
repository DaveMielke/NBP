package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DescribeCursorKeyActions extends Action {
  @Override
  public boolean performAction (int cursorKey) {
    ActionChooser.chooseAction(Endpoints.host
                                        .get()
                                        .getKeyBindings()
                                        .getRootKeyBindingMap(),
      cursorKey
    );

    return true;
  }

  public DescribeCursorKeyActions (Endpoint endpoint) {
    super(endpoint, false);
  }
}

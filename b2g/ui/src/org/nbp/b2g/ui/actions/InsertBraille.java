package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class InsertBraille extends Action {
  private final static String LOG_TAG = InsertBraille.class.getName();

  @Override
  public boolean performAction () {
    Byte dots = KeyMask.getDots(getNavigationKeys());
    if (dots == null) return false;
    return getEndpoint().insertText(Braille.toCharacter(dots));
  }

  public InsertBraille (Endpoint endpoint) {
    super(endpoint, false);
  }
}

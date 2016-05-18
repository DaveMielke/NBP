package org.nbp.b2g.ui.display;
import org.nbp.b2g.ui.*;

import android.content.Context;

public abstract class Component {
  protected final static int BYTE_MASK = 0XFF;

  protected Component () {
  }

  protected final Context getContext () {
    return ApplicationContext.getContext();
  }

  protected final DisplayEndpoint getEndpoint () {
    return Endpoints.display.get();
  }

  protected final static void message (String text) {
    ApplicationUtilities.message(text);
  }

  protected final boolean write (String text) {
    return getEndpoint().write(text);
  }
}

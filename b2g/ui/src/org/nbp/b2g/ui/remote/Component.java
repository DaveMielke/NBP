package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

import android.content.Context;

public abstract class Component {
  protected final static int BYTE_MASK = 0XFF;

  protected Component () {
  }

  protected final Context getContext () {
    return ApplicationContext.getContext();
  }

  protected final RemoteEndpoint getEndpoint () {
    return Endpoints.remote.get();
  }

  protected final static void message (String text) {
    ApplicationUtilities.message(text);
  }

  protected final boolean write (String text) {
    return getEndpoint().write(text);
  }
}

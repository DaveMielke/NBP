package org.nbp.b2g.ui;

import android.content.Context;

public abstract class UserInterfaceComponent {
  protected UserInterfaceComponent () {
  }

  protected final static Context getContext () {
    return ApplicationContext.getContext();
  }

  protected final static String getString (int resource) {
    return getContext().getString(resource);
  }
}

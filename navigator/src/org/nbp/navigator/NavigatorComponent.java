package org.nbp.navigator;

import org.nbp.common.CommonContext;

import android.util.Log;
import android.content.Context;

public abstract class NavigatorComponent {
  private final static String LOG_TAG = NavigatorComponent.class.getClass().getName();

  protected NavigatorComponent () {
  }

  public final static Context getContext () {
    return CommonContext.getContext();
  }

  public final static String getString (int resource) {
    return getContext().getString(resource);
  }
}

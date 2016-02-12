package org.nbp.editor;

import org.nbp.common.CommonContext;

import android.app.Activity;

public abstract class ApplicationContext extends CommonContext {
  private static Activity mainActivity = null;

  public static void setMainActivity (Activity activity) {
    mainActivity = activity;
  }

  private ApplicationContext () {
  }
}

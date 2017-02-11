package org.nbp.editor;

import org.nbp.common.CommonContext;

import android.content.Context;

public abstract class ApplicationUtilities {
  public static Context getContext () {
    return CommonContext.getContext();
  }

  private ApplicationUtilities () {
  }
}

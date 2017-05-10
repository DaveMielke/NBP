package org.nbp.editor;

import android.graphics.Color;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static int[] REVISION_AUTHOR_COLORS = new int[] {
    Color.RED,
    Color.BLUE
  };

  public final static boolean ASPOSE_LOG_UNHANDLED_CHILDREN = false;
}

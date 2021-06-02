package org.nbp.editor;

import android.graphics.Color;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static int[] REVIEWER_COLORS = new int[] {
    Color.RED,
    Color.BLUE,
    Color.GREEN,
    Color.YELLOW,
    Color.CYAN,
    Color.MAGENTA,
    Color.GRAY
  };

  public final static String STRING_ENCODING = "UTF8";
  public final static int RECENT_URI_LIMIT = 10;
  public final static long REVISION_JOIN_MILLISECONDS = 600000;

  public final static String ASPOSE_WORDS_LICENSE_FILE = "Aspose.Words.License";
  public final static boolean ASPOSE_LOG_UNHANDLED_CHILDREN = false;
}

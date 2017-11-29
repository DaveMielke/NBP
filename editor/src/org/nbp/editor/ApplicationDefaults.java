package org.nbp.editor;

public abstract class ApplicationDefaults {
  private ApplicationDefaults () {
  }

  public final static boolean PROTECT_TEXT = false;
  public final static int SIZE_LIMIT = 150000;

  public final static BrailleMode BRAILLE_MODE = BrailleMode.TEXT;
  public final static BrailleCode BRAILLE_CODE = BrailleCode.NONE;

  public final static String AUTHOR_NAME = "";
}

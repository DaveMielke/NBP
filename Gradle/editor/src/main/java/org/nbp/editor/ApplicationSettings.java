package org.nbp.editor;

public abstract class ApplicationSettings {
  private ApplicationSettings () {
  }

  public volatile static boolean PROTECT_TEXT = ApplicationDefaults.PROTECT_TEXT;
  public volatile static int SIZE_LIMIT = ApplicationDefaults.SIZE_LIMIT;

  public volatile static BrailleMode BRAILLE_MODE = ApplicationDefaults.BRAILLE_MODE;
  public volatile static BrailleCode BRAILLE_CODE = ApplicationDefaults.BRAILLE_CODE;

  public volatile static String AUTHOR_NAME = ApplicationDefaults.AUTHOR_NAME;
}

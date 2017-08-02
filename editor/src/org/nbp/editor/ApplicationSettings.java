package org.nbp.editor;

public abstract class ApplicationSettings {
  private ApplicationSettings () {
  }

  public volatile static boolean PROTECT_TEXT = ApplicationDefaults.PROTECT_TEXT;
  public volatile static BrailleMode BRAILLE_MODE = ApplicationDefaults.BRAILLE_MODE;
  public volatile static String AUTHOR_NAME = ApplicationDefaults.AUTHOR_NAME;
}

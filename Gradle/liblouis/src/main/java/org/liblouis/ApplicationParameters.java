package org.liblouis;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static TranslatorIdentifier DEFAULT_TRANSLATOR = InternalTranslatorIdentifier.EN_UEB_G2;
  public final static int TRANSLATION_RETRY_LIMIT = 5;
  public final static int TRANSLATION_UNUSED_LIMIT = 100;
}

package org.liblouis;

public interface TranslatorIdentifier {
  public abstract String getName ();
  public abstract String getDescription ();
  public abstract Translator getTranslator ();
}

package org.liblouis;

public class TranslationBuilder {
  private Translator selectedTranslator = null;
  private CharSequence inputCharacters = "";
  private int outputLength = 1;
  private Integer cursorOffset = null;
  private boolean includeHighlighting = false;
  private boolean noContractions = false;
  private boolean allowLongerOutput = false;

  public final Translator getTranslator () {
    if (selectedTranslator == null) {
      setTranslator(ApplicationParameters.DEFAULT_TRANSLATOR);
    }

    return selectedTranslator;
  }

  public final TranslationBuilder setTranslator (Translator translator) {
    selectedTranslator = translator;
    return this;
  }

  public final TranslationBuilder setTranslator (TranslatorIdentifier identifier) {
    return setTranslator(identifier.getTranslator());
  }

  public final TranslationBuilder setTranslator (String name) {
    return setTranslator(new InternalTranslator(name));
  }

  public final TranslationBuilder setTranslator (String forwardName, String backwardName) {
    return setTranslator(new InternalTranslator(forwardName, backwardName));
  }

  public final CharSequence getInputCharacters () {
    return inputCharacters;
  }

  public final TranslationBuilder setInputCharacters (CharSequence characters) {
    inputCharacters = characters;
    return this;
  }

  public final int getOutputLength () {
    return outputLength;
  }

  public final TranslationBuilder setOutputLength (int length) {
    outputLength = length;
    return this;
  }

  public final Integer getCursorOffset () {
    return cursorOffset;
  }

  public final TranslationBuilder setCursorOffset (int offset) {
    cursorOffset = offset;
    return this;
  }

  public final TranslationBuilder setCursorOffset () {
    cursorOffset = null;
    return this;
  }

  public final boolean getIncludeHighlighting () {
    return includeHighlighting;
  }

  public final TranslationBuilder setIncludeHighlighting (boolean yes) {
    includeHighlighting = yes;
    return this;
  }

  public final boolean getNoContractions () {
    return noContractions;
  }

  public final TranslationBuilder setNoContractions (boolean yes) {
    noContractions = yes;
    return this;
  }

  public final boolean getAllowLongerOutput () {
    return allowLongerOutput;
  }

  public final TranslationBuilder setAllowLongerOutput (boolean yes) {
    allowLongerOutput = yes;
    return this;
  }

  private final void verifyValue (boolean ok, String problem) {
    if (!ok) {
      throw new IllegalArgumentException(problem);
    }
  }

  private final void verifyValues () {
    verifyValue((selectedTranslator != null), "translator not set");
    verifyValue((inputCharacters != null), "input characters not set");
    verifyValue((outputLength >= 0), "negative output length");
    verifyValue(((cursorOffset == null) || (cursorOffset >= 0)), "negative cursor offset");
  }

  public final BrailleTranslation newBrailleTranslation () {
    verifyValues();
    return new BrailleTranslation(this);
  }

  public final TextTranslation newTextTranslation () {
    verifyValues();
    return new TextTranslation(this);
  }

  public TranslationBuilder () {
  }
}

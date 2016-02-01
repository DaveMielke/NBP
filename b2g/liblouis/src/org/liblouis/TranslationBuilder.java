package org.liblouis;

public class TranslationBuilder {
  private TranslationTable translationTable = TranslationTable.EN_UEB_G2;
  private CharSequence inputCharacters = "";
  private int outputLength = 1;
  private Integer cursorOffset = null;
  private boolean includeHighlighting = false;

  public TranslationTable getTranslationTable () {
    return translationTable;
  }

  public TranslationBuilder setTranslationTable (TranslationTable table) {
    translationTable = table;
    return this;
  }

  public CharSequence getInputCharacters () {
    return inputCharacters;
  }

  public TranslationBuilder setInputCharacters (CharSequence characters) {
    inputCharacters = characters;
    return this;
  }

  public int getOutputLength () {
    return outputLength;
  }

  public TranslationBuilder setOutputLength (int length) {
    outputLength = length;
    return this;
  }

  public Integer getCursorOffset () {
    return cursorOffset;
  }

  public TranslationBuilder setCursorOffset (int offset) {
    cursorOffset = offset;
    return this;
  }

  public TranslationBuilder setCursorOffset () {
    cursorOffset = null;
    return this;
  }

  public boolean getIncludeHighlighting () {
    return includeHighlighting;
  }

  public TranslationBuilder setIncludeHighlighting (boolean yes) {
    includeHighlighting = yes;
    return this;
  }

  private void verifyValue (boolean ok, String problem) {
    if (!ok) {
      throw new IllegalArgumentException(problem);
    }
  }

  private void verifyValues () {
    verifyValue((translationTable != null), "translation table not set");
    verifyValue((inputCharacters != null), "input characters not set");
    verifyValue((outputLength >= 0), "negative output length");
    verifyValue(((cursorOffset == null) || (cursorOffset >= 0)), "negative cursor offset");
  }

  public BrailleTranslation newBrailleTranslation () {
    verifyValues();
    return new BrailleTranslation(this);
  }

  public TextTranslation newTextTranslation () {
    verifyValues();
    return new TextTranslation(this);
  }

  public TranslationBuilder () {
  }
}

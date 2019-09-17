package org.nbp.common.dictionary;

public class ShowInfoCommand extends TextRequest {
  public ShowInfoCommand (String database) {
    super("show", "info", database);
  }

  public ShowInfoCommand (DictionaryDatabase database) {
    this(database.getName());
  }

  @Override
  protected final int getResponseCode () {
    return ResponseCodes.BEGIN_DATABASE_TEXT;
  }
}

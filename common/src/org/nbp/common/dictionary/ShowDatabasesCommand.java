package org.nbp.common.dictionary;

public class ShowDatabasesCommand extends ItemsRequest {
  public ShowDatabasesCommand () {
    super("show", "databases");
  }

  @Override
  protected final int getResponseCode () {
    return ResponseCodes.BEGIN_DATABASE_LIST;
  }
}

package org.nbp.common.dictionary;

public class ShowServerCommand extends TextRequest {
  public ShowServerCommand () {
    super("show", "server");
  }

  @Override
  protected final int getResponseCode () {
    return ResponseCodes.BEGIN_SERVER_TEXT;
  }
}

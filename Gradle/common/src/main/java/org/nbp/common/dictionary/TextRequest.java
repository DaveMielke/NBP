package org.nbp.common.dictionary;

public abstract class TextRequest extends CommandRequest implements TextHandler {
  private String savedText = null;

  protected TextRequest (String... arguments) {
    super(arguments);
  }

  public final String getText () {
    return savedText;
  }

  @Override
  public void handleText (String text) {
  }

  @Override
  protected final void handleResult () {
    handleText(getText());
  }

  protected final void saveText () {
    savedText = getTextAsString();
  }
}

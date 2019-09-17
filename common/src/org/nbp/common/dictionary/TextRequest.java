package org.nbp.common.dictionary;

public abstract class TextRequest extends CommandRequest implements TextHandler {
  protected abstract int getResponseCode ();

  private String text = null;

  protected TextRequest (String... arguments) {
    super(arguments);
  }

  public final String getText () {
    return text;
  }

  @Override
  public void handleText (String text) {
  }

  @Override
  protected final void handleResult () {
    handleText(getText());
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    final int begin = getResponseCode();

    if (code == begin) {
      text = getTextAsString();
      return false;
    }

    return super.handleResponse(code, operands);
  }
}

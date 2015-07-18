package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

public abstract class PromptEndpoint extends Endpoint {
  public abstract boolean handleResponse (String response);

  private final StringBuilder buffer = new StringBuilder();
  private final int start;

  @Override
  public boolean write () {
    setText(buffer.toString(), true);
    return super.write();
  }

  @Override
  public void onForeground () {
    setSelection(start, buffer.length());
    super.onForeground();
  }

  @Override
  public boolean insertText (String string) {
    int start = getSelectionStart();
    int end = getSelectionEnd();

    buffer.delete(start, end);
    buffer.insert(start, string);
    if (!setCursor(start + string.length())) return false;
    return write();
  }

  @Override
  public boolean deleteText (int start, int end) {
    buffer.delete(start, end);
    if (!setCursor(start)) return false;
    return write();
  }

  public String getResponse () {
    return buffer.toString().substring(start);
  }

  @Override
  public boolean isEditable () {
    return true;
  }

  @Override
  public boolean isSelectable (int offset) {
    return offset >= start;
  }

  @Override
  public boolean handleKeyboardKey_enter () {
    boolean success = handleResponse(getResponse());
    if (!super.handleKeyboardKey_enter()) return false;
    return success;
  }

  @Override
  protected String[] getKeysFileNames () {
    return new String[] {
      "nabcc", "common", "speech", "edit"
    };
  }

  public PromptEndpoint (String prompt) {
    super();

    buffer.append(prompt);
    buffer.append("> ");
    start = buffer.length();
  }
}

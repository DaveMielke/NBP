package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

public abstract class PromptEndpoint extends Endpoint {
  private final StringBuilder buffer = new StringBuilder();
  private final int start;

  protected String getTrailer () {
    return "";
  }

  @Override
  public final void onForeground () {
    setSelection(start, buffer.length());
    super.onForeground();
  }

  protected final int getResponseLength () {
    return buffer.length() - start;
  }

  protected boolean canInsertText (CharSequence text) {
    return true;
  }

  private final void setText () {
    setText((buffer.toString() + getTrailer()), true);
  }

  @Override
  public final boolean replaceText (int start, int end, CharSequence text) {
    if (!canInsertText(text)) return false;

    buffer.delete(start, end);
    buffer.insert(start, text);

    setText();
    if (!setCursor((start + text.length()))) return false;
    return write();
  }

  protected final String getResponse () {
    return buffer.toString().substring(start);
  }

  @Override
  public final boolean isInputArea () {
    return true;
  }

  @Override
  public final boolean isSelectable (int offset) {
    if (offset < start) return false;
    if (offset > buffer.length()) return false;
    return true;
  }

  protected abstract boolean handleResponse (String response);

  protected final boolean handleResponse () {
    return handleResponse(getResponse());
  }

  @Override
  public final boolean handleKeyboardKey_enter () {
    try {
      return handleResponse();
    } finally {
      if (!super.handleKeyboardKey_enter()) return false;
    }
  }

  public PromptEndpoint (int prompt, String prefix) {
    super("prompt");

    buffer.append(ApplicationContext.getString(prompt));
    buffer.append("> ");
    buffer.append(prefix);

    start = buffer.length();
    setText();
  }

  public PromptEndpoint (int prompt) {
    this(prompt, "");
  }
}

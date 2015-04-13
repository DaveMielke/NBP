package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

public abstract class PromptEndpoint extends Endpoint {
  public abstract boolean handleResponse (String response);

  private final StringBuilder buffer = new StringBuilder();
  private final int start;

  public boolean flush () {
    setText(buffer.toString(), getLineIndent());
    return write();
  }

  @Override
  public void onForeground () {
    setCursor(buffer.length());
    flush();
  }

  @Override
  public boolean insertText (String string) {
    int cursor = getSelectionEnd();
    buffer.insert(cursor, string);
    if (!setCursor(cursor + string.length())) return false;
    return flush();
  }

  @Override
  public boolean deleteText (int start, int end) {
    buffer.delete(start, end);
    if (!setCursor(start)) return false;
    return flush();
  }

  public boolean deletePrevious (boolean all) {
    int cursor = getSelectionEnd();
    if (cursor == start) return false;

    if (all) {
      buffer.delete(start, cursor);
      cursor = start;
    } else {
      buffer.deleteCharAt(--cursor);
    }

    setCursor(cursor);
    return flush();
  }

  public boolean deleteNext (boolean all) {
    int cursor = getSelectionEnd();
    int length = buffer.length();
    if (cursor == length) return false;

    if (all) {
      buffer.delete(cursor, length);
    } else {
      buffer.deleteCharAt(cursor);
    }

    return flush();
  }

  public boolean bringCursor (int cursorKey) {
    int offset = getBrailleStart() + cursorKey;
    if (offset < start) return false;
    if (offset > getTextLength()) return false;
    setCursor(offset);
    return flush();
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

  private final static String[] keysFileNames = new String[] {
    "nabcc", "common", "edit"
  };

  @Override
  protected String[] getKeysFileNames () {
    return keysFileNames;
  }

  public PromptEndpoint (String prompt) {
    super();

    buffer.append(prompt);
    buffer.append("> ");
    start = buffer.length();
  }
}

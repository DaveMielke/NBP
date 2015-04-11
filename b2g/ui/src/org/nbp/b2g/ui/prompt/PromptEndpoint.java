package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

public abstract class PromptEndpoint extends Endpoint {
  public abstract boolean handleResponse (String response);

  private final StringBuilder buffer = new StringBuilder();
  private final int start;
  private int cursor;

  public boolean flush () {
    setText(buffer.toString(), getLineIndent());
    setCursor(cursor);
    return write();
  }

  @Override
  public void onForeground () {
    cursor = buffer.length();
    flush();
  }

  public boolean insertCharacter (char character) {
    buffer.insert(cursor++, character);
    return flush();
  }

  public boolean deletePrevious (boolean all) {
    if (cursor == start) return false;

    if (all) {
      buffer.delete(start, cursor);
      cursor = start;
    } else {
      buffer.deleteCharAt(--cursor);
    }

    return flush();
  }

  public boolean deleteNext (boolean all) {
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
    cursor = offset;
    return flush();
  }

  public String getResponse () {
    return buffer.toString().substring(start);
  }

  @Override
  public boolean isEditable () {
    return true;
  }

  private final static String[] keysFileNames = new String[] {
    "nabcc", "all", "prompt"
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

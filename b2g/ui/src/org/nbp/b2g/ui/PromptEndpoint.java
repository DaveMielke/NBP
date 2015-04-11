package org.nbp.b2g.ui;

public class PromptEndpoint extends Endpoint {
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

  public boolean deletePrevious () {
    if (cursor == start) return false;
    buffer.deleteCharAt(--cursor);
    return flush();
  }

  public boolean deleteNext () {
    if (cursor == buffer.length()) return false;
    buffer.deleteCharAt(cursor);
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

  public PromptEndpoint (String prompt) {
    super();

    buffer.append(prompt);
    buffer.append("> ");
    start = buffer.length();
  }
}

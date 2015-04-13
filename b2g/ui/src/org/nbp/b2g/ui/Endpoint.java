package org.nbp.b2g.ui;

public abstract class Endpoint {
  public boolean isEditable () {
    return false;
  }

  public boolean insertCharacter (char character) {
    return false;
  }

  public boolean write () {
    synchronized (Endpoints.LOCK) {
      if (this != Endpoints.getCurrentEndpoint()) return true;

      synchronized (this) {
        return BrailleDevice.write();
      }
    }
  }

  public void onForeground () {
    write();
  }

  public void onBackground () {
  }

  private final KeyBindings keyBindings;

  protected String[] getKeysFileNames () {
    return null;
  }

  public KeyBindings getKeyBindings () {
    return keyBindings;
  }

  private final Characters characters;

  public Characters getCharacters () {
    return characters;
  }

  private String textString;
  private boolean softEdges;

  public String getText () {
    return textString;
  }

  public int getTextLength () {
    return textString.length();
  }

  private String lineText;
  private int lineStart;
  private int lineIndent;

  public int findPreviousNewline (int offset) {
    return textString.lastIndexOf('\n', offset-1);
  }

  public int findNextNewline (int offset) {
    return textString.indexOf('\n', offset);
  }

  public int setLine (int textOffset) {
    lineStart = findPreviousNewline(textOffset) + 1;

    int lineEnd = findNextNewline(lineStart);
    if (lineEnd == -1) lineEnd = getTextLength();

    lineText = textString.substring(lineStart, lineEnd);
    return textOffset - lineStart;
  }

  protected void setText (String text, int indent) {
    textString = text;
    softEdges = false;

    setLine(0);
    lineIndent = indent;
  }

  protected void setText (String text) {
    setText(text, 0);
  }

  public boolean hasSoftEdges () {
    return softEdges || isEditable();
  }

  public String getLineText () {
    return lineText;
  }

  public int getLineStart () {
    return lineStart;
  }

  public int getLineLength () {
    return lineText.length();
  }

  public int getLineIndent () {
    return lineIndent;
  }

  public void setLineIndent (int indent) {
    lineIndent = indent;
  }

  public int getBrailleStart () {
    return lineStart + lineIndent;
  }

  public final static int NO_SELECTION = -1;
  private int selectionStart = NO_SELECTION;
  private int selectionEnd = NO_SELECTION;

  public int getSelectionStart () {
    return selectionStart;
  }

  public int getSelectionEnd () {
    return selectionEnd;
  }

  public static boolean isSelected (int offset) {
    return offset != NO_SELECTION;
  }

  public static boolean isSelected (int start, int end) {
    return (start != end) && isSelected(start) && isSelected(end);
  }

  public boolean isSelected () {
    synchronized (this) {
      return isSelected(selectionStart, selectionEnd);
    }
  }

  public String getSelectedText () {
    synchronized (this) {
      if (isSelected()) {
        return textString.substring(selectionStart, selectionEnd);
      }
    }

    return null;
  }

  protected void adjustLeft (int offset, int keep) {
    if (offset < lineIndent) {
      int newIndent = offset - keep;
      if (newIndent < 0) newIndent = 0;
      lineIndent = newIndent;
    }
  }

  protected void adjustRight (int offset, int keep) {
    int brailleLength = BrailleDevice.size();

    if (offset >= (lineIndent + brailleLength)) {
      lineIndent = offset + keep - brailleLength;
    }
  }

  protected void adjustScroll (int offset) {
    int keep = ApplicationParameters.BRAILLE_SCROLL_KEEP;
    adjustLeft(offset, keep);
    adjustRight(offset, keep);
  }

  public boolean setSelection (int start, int end) {
    synchronized (this) {
      selectionStart = start;
      selectionEnd = end;

      if ((start == end) && isSelected(start)) {
        adjustScroll(setLine(start));
      }

      return write();
    }
  }

  public boolean clearSelection () {
    return setSelection(NO_SELECTION, NO_SELECTION);
  }

  public boolean setCursor (int offset) {
    return setSelection(offset, offset);
  }

  public boolean write (String text) {
    synchronized (this) {
      setText(text);
      clearSelection();
      softEdges = true;
      return write();
    }
  }

  public boolean scrollRight (int offset) {
    if (offset < 1) return false;
    if (offset >= BrailleDevice.size()) return false;

    if ((offset += getLineIndent()) >= getLineLength()) return false;
    setLineIndent(offset);
    return write();
  }

  public boolean panLeft () {
    int indent = getLineIndent();

    if (indent == 0) {
      int start = getLineStart();
      if (start == 0) return false;

      setLine(start-1);
      indent = getLineLength() + 1;
    } else {
      int length = getLineLength();
      if (indent > length) indent = length;
    }

    if ((indent -= BrailleDevice.size()) < 0) indent = 0;
    setLineIndent(indent);
    return write();
  }

  public boolean panRight () {
    int indent = getLineIndent() + BrailleDevice.size();
    int length = getLineLength();

    if (indent > length) {
      int offset = getLineStart() + length + 1;
      if (offset > getTextLength()) return false;

      setLine(offset);
      indent = 0;
    }

    setLineIndent(indent);
    return write();
  }

  public Endpoint () {
    characters = new Characters();
    keyBindings = new KeyBindings(this, getKeysFileNames());
    setText("");
  }
}

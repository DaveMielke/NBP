package org.nbp.b2g.ui;
import org.nbp.b2g.ui.host.HostEndpoint;

public class Endpoint {
  private final static HostEndpoint hostEndpoint = new HostEndpoint();
  private static Endpoint currentEndpoint = hostEndpoint;

  private final static Object LOCK = new Object();

  public static Endpoint getCurrentEndpoint () {
    synchronized (LOCK) {
      return currentEndpoint;
    }
  }

  public static HostEndpoint getHostEndpoint () {
    synchronized (LOCK) {
      return hostEndpoint;
    }
  }

  public boolean write () {
    synchronized (this) {
      if (this != currentEndpoint) return true;
      return BrailleDevice.write(this);
    }
  }

  public boolean write (String text) {
    synchronized (this) {
      setText(text);
      clearSelection();
      return write();
    }
  }

  public boolean isEditable () {
    return false;
  }

  private KeyBindings keyBindings = null;

  protected String[] getKeysFileNames () {
    return null;
  }

  private void ensureKeyBindings () {
    synchronized (this) {
      if (keyBindings == null) {
        keyBindings = new KeyBindings(this);
        keyBindings.addKeyBindings(getKeysFileNames());
      }
    }
  }

  public KeyBindings getKeyBindings () {
    ensureKeyBindings();
    return keyBindings;
  }

  private final Characters characters = new Characters();

  public Characters getCharacters () {
    ensureKeyBindings();
    return characters;
  }

  private String textString;

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
    int lineLength = findNextNewline(lineStart);
    if (lineLength == -1) lineLength = getTextLength();
    lineText = textString.substring(lineStart, lineLength);
    return textOffset - lineStart;
  }

  protected void setText (String text, int indent) {
    textString = text;
    setLine(0);
    lineIndent = indent;
  }

  protected void setText (String text) {
    setText(text, 0);
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

  public int getBrailleStart () {
    return lineStart + lineIndent;
  }

  public void setLineIndent (int indent) {
    lineIndent = indent;
  }

  public final static int NO_SELECTION = -1;
  private int selectionStart = NO_SELECTION;
  private int selectionEnd = NO_SELECTION;

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

  public int getSelectionStart () {
    return selectionStart;
  }

  public int getSelectionEnd () {
    return selectionEnd;
  }

  public String getSelectedText () {
    synchronized (LOCK) {
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
    int brailleLength = BrailleDevice.getBrailleLength();

    if (offset >= (lineIndent + brailleLength)) {
      lineIndent = offset + keep - brailleLength;
    }
  }

  public void setSelection (int start, int end) {
    synchronized (LOCK) {
      selectionStart = start;
      selectionEnd = end;

      if ((start == end) && isSelected(start)) {
        int offset = setLine(start);
        int keep = ApplicationParameters.BRAILLE_SCROLL_KEEP;

        adjustLeft(offset, keep);
        adjustRight(offset, keep);
      }

      write();
    }
  }

  public void clearSelection () {
    setSelection(NO_SELECTION, NO_SELECTION);
  }

  public Endpoint () {
    setText("");
  }
}

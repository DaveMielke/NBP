package org.nbp.b2g.ui;
import org.nbp.b2g.ui.host.HostEndpoint;

public class Endpoint {
  private final static Object LOCK = new Object();

  private final static HostEndpoint hostEndpoint = new HostEndpoint();
  private final static BluetoothEndpoint bluetoothEndpoint = new BluetoothEndpoint();
  private static Endpoint currentEndpoint = hostEndpoint;

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
    synchronized (LOCK) {
      if (this != currentEndpoint) return true;

      synchronized (this) {
        return BrailleDevice.write(this);
      }
    }
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

    int lineEnd = findNextNewline(lineStart);
    if (lineEnd == -1) lineEnd = getTextLength();

    lineText = textString.substring(lineStart, lineEnd);
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

  protected void setLineIndent (int indent) {
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
    setText("");
  }
}

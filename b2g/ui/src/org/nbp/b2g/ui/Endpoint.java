package org.nbp.b2g.ui;

import android.util.Log;

public abstract class Endpoint {
  private final static String LOG_TAG = Endpoint.class.getName();

  private String oldText;
  private int oldLineStart;
  private int oldSelectionStart;
  private int oldSelectionEnd;

  protected void resetSpeech () {
    oldText = "";
    oldLineStart = -1;
    oldSelectionStart = NO_SELECTION;
    oldSelectionEnd = NO_SELECTION;
  }

  private void say () {
    synchronized (this) {
      String text = null;

      final String newText = getText();
      final int newLength = newText.length();

      final int newLineStart = getLineStart();
      final int newSelectionStart = getSelectionStart();
      final int newSelectionEnd = getSelectionEnd();

      int start = 0;
      int oldEnd = oldText.length();
      int newEnd = newLength;

      while ((start < oldEnd) && (start < newEnd)) {
        if (oldText.charAt(start) != newText.charAt(start)) break;
        start += 1;
      }

      while ((oldEnd > start) && (newEnd > start)) {
        if (oldText.charAt(--oldEnd) != newText.charAt(--newEnd)) {
          oldEnd += 1;
          newEnd += 1;
          break;
        }
      }

      if (newEnd > start) {
        int from = Math.max(start, (newLineStart - 1));
        int to = Math.min(newEnd, (newLineStart + getLineLength()));

        from = Math.min(from, to);
        to = Math.max(to, from);

        text = newText.substring(from, to);
      } else if (oldEnd > start) {
        int from = Math.max(start, newLineStart);
        int to = Math.max(oldEnd, start);

        from = Math.min(from, to);
        to = Math.max(to, from);

        text = oldText.substring(from, to);
      } else if (isSelected(newSelectionStart) && isSelected(newSelectionEnd)) {
        int offset = NO_SELECTION;

        if (newSelectionStart != oldSelectionStart) {
          offset = newSelectionStart;
        } else if (newSelectionEnd != oldSelectionEnd) {
          offset = newSelectionEnd - 1;
        }

        if (offset != NO_SELECTION) {
          if (offset < newLength) {
            text = newText.substring(offset, offset+1);
          } else if (offset == newLength) {
            text = ApplicationContext.getString(R.string.character_end);
          }
        }
      }

      if (text == null) {
        if (newLineStart != oldLineStart) {
          text = getLineText();
        }
      }

      if (text != null) {
        if (text.length() == 1) {
          switch (text.charAt(0)) {
            case '\n':
              text = ApplicationContext.getString(R.string.character_newline);
              break;
          }
        }

        if (text.length() > 0) {
          SpeechDevice speech = Devices.speech.get();

          synchronized (speech) {
            speech.stopSpeaking();
            speech.say(text);
          }
        }
      }

      oldText = newText;
      oldLineStart = newLineStart;
      oldSelectionStart = newSelectionStart;
      oldSelectionEnd = newSelectionEnd;
    }
  }

  public boolean isEditable () {
    return false;
  }

  public boolean isSeekable () {
    return false;
  }

  public boolean seekNext () {
    return false;
  }

  public boolean seekPrevious () {
    return false;
  }

  public boolean insertText (String string) {
    return false;
  }

  public boolean insertText (char character) {
    return insertText(Character.toString(character));
  }

  public boolean deleteText (int start, int end) {
    return false;
  }

  public boolean deleteSelectedText () {
    if (!isSelected()) return false;
    return deleteText(getSelectionStart(), getSelectionEnd());
  }

  public boolean deleteText (int position) {
    int start = getSelectionStart();
    int end = getSelectionEnd();

    if (!isSelected(start, end)) {
      if (!isSelected(end)) return false;
      start = end + position;
      end = start + 1;

      if (start < 0) return false;
      if (end > getTextLength()) return false;
      if (!isSelectable(start)) return false;
    }

    return deleteText(start, end);
  }

  public boolean write () {
    Endpoints.READ_LOCK.lock();
    try {
      if (this != Endpoints.getCurrentEndpoint()) return true;
      say();
      return Devices.braille.get().write();
    } finally {
      Endpoints.READ_LOCK.unlock();
    }
  }

  public void onForeground () {
    resetSpeech();
    write();
  }

  public void onBackground () {
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

  protected void setText (String text, boolean stay) {
    textString = text;
    softEdges = false;

    {
      int start = 0;

      if (stay) {
        if ((lineStart > 0) && (lineStart <= text.length())) {
          if (text.charAt(lineStart-1) == '\n') {
            start = lineStart;
          }
        }
      }

      setLine(start);
    }

    {
      int indent = 0;

      if (stay) {
        if (lineIndent < lineText.length()) {
          indent = lineIndent;
        }
      }

      lineIndent = indent;
    }
  }

  protected void setText (String text) {
    setText(text, false);
    resetSpeech();
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
      if (isEditable() && isSelected()) {
        return textString.substring(selectionStart, selectionEnd);
      }
    }

    return null;
  }

  public int getTextOffset (int cursorKey) {
    return getBrailleStart() + cursorKey;
  }

  public int toLineOffset (int textOffset) {
    return textOffset - getLineStart();
  }

  public boolean isCharacterOffset (int textOffset) {
    int lineOffset = toLineOffset(textOffset);
    return ((lineOffset >= 0) && (lineOffset < getLineLength()));
  }

  public boolean isCursorOffset (int textOffset) {
    int lineOffset = toLineOffset(textOffset);
    return ((lineOffset >= 0) && (lineOffset <= getLineLength()));
  }

  protected void adjustLeft (int offset, int keep) {
    if (offset < lineIndent) {
      int newIndent = offset - keep;
      if (newIndent < 0) newIndent = 0;
      lineIndent = newIndent;
    }
  }

  protected void adjustRight (int offset, int keep) {
    int brailleLength = Devices.braille.get().size();

    if (offset >= (lineIndent + brailleLength)) {
      lineIndent = offset + keep - brailleLength;
    }
  }

  protected void adjustScroll (int offset) {
    int keep = ApplicationParameters.BRAILLE_SCROLL_KEEP;
    adjustLeft(offset, keep);
    adjustRight(offset, keep);
  }

  public boolean onSelectionChange (int start, int end) {
    synchronized (this) {
      if (ApplicationSettings.LOG_UPDATES) {
        Log.d(LOG_TAG, String.format(
          "selection changed: Start:%d->%d End:%d->%d",
          selectionStart, start, selectionEnd, end
        ));
      }

      if ((start == selectionStart) && (end == selectionEnd)) return true;

      selectionStart = start;
      selectionEnd = end;

      if ((start == end) && isSelected(start)) {
        adjustScroll(setLine(start));
      }
    }

    return write();
  }

  public boolean clearSelection () {
    return onSelectionChange(NO_SELECTION, NO_SELECTION);
  }

  public boolean isSelectable (int offset) {
    return true;
  }

  public boolean setSelection (int start, int end) {
    boolean startSelected = isSelected(start);
    boolean endSelected = isSelected(end);
    if (startSelected != endSelected) return false;

    if (startSelected && !isSelectable(start)) return false;
    if (endSelected && !isSelectable(end)) return false;
    return onSelectionChange(start, end);
  }

  public boolean setCursor (int offset) {
    return setSelection(offset, offset);
  }

  public boolean write (String text) {
    synchronized (this) {
      setText(text);
      clearSelection();
      softEdges = true;
    }

    return write();
  }

  public boolean write (int resource) {
    return write(ApplicationContext.getString(resource));
  }

  private abstract class Panner {
    protected abstract boolean moveDisplay ();
    protected abstract String getEndAction ();

    public final boolean pan () {
      synchronized (Endpoint.this) {
        if (moveDisplay()) return write();
        if (hasSoftEdges()) return false;
      }

      return KeyEvents.performAction(getEndAction(), Endpoint.this);
    }
  }

  protected String getPanLeftEndAction () {
    return null;
  }

  public boolean panLeft () {
    Panner panner = new Panner() {
      @Override
      protected boolean moveDisplay () {
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

        if ((indent -= Devices.braille.get().size()) < 0) indent = 0;
        setLineIndent(indent);
        return true;
      }

      @Override
      protected String getEndAction () {
        return getPanLeftEndAction();
      }
    };

    return panner.pan();
  }

  protected String getPanRightEndAction () {
    return null;
  }

  public boolean panRight () {
    Panner panner = new Panner() {
      @Override
      protected boolean moveDisplay () {
        int indent = getLineIndent() + Devices.braille.get().size();
        int length = getLineLength();

        if (indent > length) {
          int offset = getLineStart() + length + 1;
          if (offset > getTextLength()) return false;

          setLine(offset);
          indent = 0;
        }

        setLineIndent(indent);
        return true;
      }

      @Override
      protected String getEndAction () {
        return getPanRightEndAction();
      }
    };

    return panner.pan();
  }

  public boolean scrollRight (int offset) {
    if (offset < 1) return false;
    if (offset >= Devices.braille.get().size()) return false;

    if ((offset += getLineIndent()) >= getLineLength()) return false;
    setLineIndent(offset);
    return write();
  }

  public boolean handleKeyboardKey_enter () {
    return false;
  }

  public boolean handleKeyboardKey_cursorLeft () {
    return KeyEvents.performAction("ArrowLeft");
  }

  public boolean handleKeyboardKey_cursorRight () {
    return KeyEvents.performAction("ArrowRight");
  }

  public boolean handleKeyboardKey_cursorUp () {
    return KeyEvents.performAction("ArrowUp");
  }

  public boolean handleKeyboardKey_cursorDown () {
    return KeyEvents.performAction("ArrowDown");
  }

  public boolean handleKeyboardKey_pageUp () {
    return false;
  }

  public boolean handleKeyboardKey_pageDown () {
    return false;
  }

  public boolean handleKeyboardKey_home () {
    return false;
  }

  public boolean handleKeyboardKey_end () {
    return false;
  }

  private final KeyBindings keyBindings;

  public KeyBindings getKeyBindings () {
    return keyBindings;
  }

  protected String[] getKeysFileNames () {
    return null;
  }

  public Endpoint () {
    characters = new Characters();
    keyBindings = new KeyBindings(this, getKeysFileNames());
    setText("");
  }
}

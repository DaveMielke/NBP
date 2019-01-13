package org.nbp.b2g.ui;
import org.nbp.b2g.ui.actions.*;

import android.util.Log;
import static org.nbp.common.LogUtilities.logText;
import static org.liblouis.Tests.logOffsets;

import org.liblouis.Translation;
import org.liblouis.BrailleTranslation;
import org.liblouis.TextTranslation;

import org.nbp.common.Braille;
import org.nbp.common.CharacterUtilities;

public abstract class Endpoint extends UserInterfaceComponent {
  private final static String LOG_TAG = Endpoint.class.getName();

  private CharSequence oldLineText;
  private int oldLineStart;
  private int oldSelectionStart;
  private int oldSelectionEnd;
  private int speechTextStart;
  private int speechTextEnd;
  private boolean sayUnchangedLine;

  private final static void logSpeech (String what, CharSequence text) {
    if (ApplicationSettings.LOG_SPEECH) {
      StringBuilder sb = new StringBuilder();
      sb.append("speech: ");
      sb.append(what);

      if (text != null) {
        sb.append(": ");
        sb.append(text);
      }

      Log.v(LOG_TAG, sb.toString());
    }
  }

  private final static void logSpeech (String what) {
    logSpeech(what, null);
  }

  protected final void resetSpeech () {
    oldLineText = "";
    oldLineStart = -1;

    oldSelectionStart = NO_SELECTION;
    oldSelectionEnd = NO_SELECTION;

    speechTextStart = NO_SELECTION;
    speechTextEnd = NO_SELECTION;

    sayUnchangedLine = false;
  }

  private final void setSayUnchangedLine () {
    sayUnchangedLine = true;
  }

  public final void setSpeechText (int start, int end) {
    speechTextStart = start;
    speechTextEnd = end;
  }

  private final CharSequence getCompletedWord (CharSequence text, int to) {
    if (--to < 1) return null;
    if (!Character.isWhitespace(text.charAt(to))) return null;

    int from = to - 1;
    if (Character.isWhitespace(text.charAt(from))) return null;

    while (from > 0) {
      if (Character.isWhitespace(text.charAt(--from))) {
        from += 1;
        break;
      }
    }

    return text.subSequence(from, to);
  }

  private final void say () {
    synchronized (this) {
      CharSequence text = null;
      boolean echo = true;
      CharSequence word = null;
      int action = 0;
      String what = null;

      final CharSequence newLineText = getLineText();
      final int newLineStart = getLineStart();
      final int newSelectionStart = getSelectionStart();
      final int newSelectionEnd = getSelectionEnd();

      if (speechTextStart != NO_SELECTION) {
        final CharSequence newText = getText();

        if ((0 <= speechTextStart) &&
            (speechTextStart < speechTextEnd) &&
            (speechTextEnd <= newText.length())
           ) {
          text = newText.subSequence(speechTextStart, speechTextEnd);
          echo = ApplicationSettings.ECHO_SELECTION;
          what = "selected text";
        }
      }

      if (text == null) {
        if (newLineStart != oldLineStart) {
          text = newLineText;
          echo = ApplicationSettings.SPEAK_LINES;
          what = "different line";
        } else {
          int start = 0;
          int oldEnd = oldLineText.length();
          int newEnd = newLineText.length();

          while ((start < oldEnd) && (start < newEnd)) {
            if (oldLineText.charAt(start) != newLineText.charAt(start)) break;
            start += 1;
          }

          while ((oldEnd > start) && (newEnd > start)) {
            if (oldLineText.charAt(--oldEnd) != newLineText.charAt(--newEnd)) {
              oldEnd += 1;
              newEnd += 1;
              break;
            }
          }

          if (newEnd > start) {
            text = newLineText.subSequence(start, newEnd);
            echo = ApplicationSettings.ECHO_CHARACTERS;

            if (text.length() == 1) {
              text = CharacterPhrase.get(text.charAt(0));
              what = "inserted character";
            } else {
              what = "inserted text";
            }

            if (ApplicationSettings.ECHO_WORDS) {
              if (start == oldEnd) {
                if (newSelectionStart == newSelectionEnd) {
                  int position = newSelectionStart - newLineStart;

                  if (position >= 0) {
                    while (newEnd > position) {
                      if (start == 0) break;

                      if (oldLineText.charAt(--oldEnd) != newLineText.charAt(--newEnd)) {
                        oldEnd += 1;
                        newEnd += 1;
                        break;
                      }

                      start -= 1;
                    }
                  }
                }
              }

              word = getCompletedWord(getText(), (newLineStart + newEnd));
            }
          } else if (oldEnd > start) {
            text = oldLineText.subSequence(start, oldEnd);
            echo = ApplicationSettings.ECHO_DELETIONS;
            action = R.string.DeleteCharacter_action_confirmation;

            if (text.length() == 1) {
              text = CharacterPhrase.get(text.charAt(0));
              what = "deleted character";
            } else {
              what = "deleted text";
            }
          } else if (isSelected(newSelectionStart) && isSelected(newSelectionEnd)) {
            int offset = NO_SELECTION;

            if (newSelectionStart != oldSelectionStart) {
              offset = newSelectionStart;

              if (newSelectionStart == newSelectionEnd) {
                what = "current character";
              } else {
                what = "first character";
                action = R.string.SetSelectionStart_action_confirmation;
              }
            } else if (newSelectionEnd != oldSelectionEnd) {
              offset = newSelectionEnd - 1;
              action = R.string.SetSelectionEnd_action_confirmation;
              what = "last character";
            } else if (sayUnchangedLine) {
              text = newLineText;
              echo = ApplicationSettings.SPEAK_LINES;
              what = "unchanged line";
            }

            if (offset != NO_SELECTION) {
              final CharSequence newText = getText();
              final int newLength = newText.length();

              if (offset < newLength) {
                text = CharacterPhrase.get(newText.charAt(offset));
              } else if (offset == newLength) {
                text = getString(R.string.character_end);
              }

              if (text != null) echo = ApplicationSettings.ECHO_SELECTION;
            }
          }
        }
      }

      if (text != null) {
        logSpeech(what, text);
        if (word != null) logSpeech("completed word", word);

        ApplicationUtilities.say(
          echo? text: null,
          word,
          (echo && (action != 0))? getString(action): null
        );
      }

      oldLineText = newLineText;
      oldLineStart = newLineStart;
      oldSelectionStart = newSelectionStart;
      oldSelectionEnd = newSelectionEnd;
      speechTextStart = NO_SELECTION;
      speechTextEnd = NO_SELECTION;
      sayUnchangedLine = false;
    }
  }

  private BrailleTranslation brailleTranslation = null;
  private TextTranslation textTranslation = null;

  private final void discardTranslation () {
    brailleTranslation = null;
    textTranslation = null;
  }

  private final boolean retrieveTranslation () {
    Translation translation = TranslationCache.get(lineText);

    if (translation != null) {
      if (translation instanceof BrailleTranslation) {
        brailleTranslation = (BrailleTranslation)translation;
        return true;
      }

      if (translation instanceof TextTranslation) {
        textTranslation = (TextTranslation)translation;
        return true;
      }
    }

    return false;
  }

  private final void makeTranslation () {
    brailleTranslation = TranslationUtilities.newBrailleTranslation(lineText, !isInputArea());
    TranslationCache.put(lineText, brailleTranslation);
  }

  private final void refreshBrailleTranslation () {
    discardTranslation();

    if (ApplicationSettings.LITERARY_BRAILLE) {
      if (retrieveTranslation()) return;
      makeTranslation();
    }
  }

  private final boolean replaceLine (CharSequence newText, int cursor) {
    CharSequence oldText = getLineText();
    int oldTo = oldText.length();
    int newTo = newText.length();
    int from = 0;

    while ((from < oldTo) && (from < newTo)) {
      if (oldText.charAt(from) != newText.charAt(from)) break;
      from += 1;
    }

    while ((from < oldTo) && (from < newTo) && (newTo > cursor)) {
      int oldLast = oldTo - 1;
      int newLast = newTo - 1;
      if (oldText.charAt(oldLast) != newText.charAt(newLast)) break;
      oldTo = oldLast;
      newTo = newLast;
    }

    boolean removing = oldTo > from;
    boolean inserting = newTo > from;
    if (!(inserting || removing)) return true;
    CharSequence newSegment = newText.subSequence(from, newTo);

    if (ApplicationSettings.LOG_ACTIONS) {
      CharSequence oldSegment = oldText.subSequence(from, oldTo);

      if (removing) {
        if (inserting) {
          Log.v(LOG_TAG, String.format(
            "replacing text: %s -> %s", oldSegment, newSegment
          ));
        } else {
          Log.v(LOG_TAG, String.format(
            "removing text: %s", oldSegment
          ));
        }
      } else {
        Log.v(LOG_TAG, String.format(
          "inserting text: %s", newSegment
        ));
      }
    }

    int start = getLineStart();
    return replaceText((start + from), (start + oldTo), newSegment);
  }

  public final boolean setBrailleCharacters (CharSequence braille, int cursor) {
    textTranslation = TranslationUtilities.newTextTranslation(braille);
    brailleTranslation = null;

    CharSequence text = textTranslation.getTextWithSpans();
    TranslationCache.put(text, textTranslation);

    if (false) {
      logText("brl", braille);
      logText("csm", textTranslation.getConsumedInput());
      logText("txt", text);
      logOffsets(textTranslation);
    }

    return replaceLine(text, textTranslation.getTextOffset(cursor));
  }

  public final CharSequence getBrailleCharacters () {
    if (textTranslation != null) return textTranslation.getConsumedBraille();
    if (brailleTranslation != null) return brailleTranslation.getBrailleWithSpans();
    return getLineText();
  }

  public final int getBrailleLength () {
    return getBrailleCharacters().length();
  }

  public final int getBrailleOffset (int textOffset) {
    if (textTranslation != null) return textTranslation.getBrailleOffset(textOffset);
    if (brailleTranslation != null) return brailleTranslation.getBrailleOffset(textOffset);
    return textOffset;
  }

  public final CharSequence getLineCharacters () {
    if (textTranslation != null) return textTranslation.getTextWithSpans();
    if (brailleTranslation != null) return brailleTranslation.getConsumedText();
    return getLineText();
  }

  public final int getLineOffset (int brailleOffset) {
    if (textTranslation != null) return textTranslation.getTextOffset(brailleOffset);
    if (brailleTranslation != null) return brailleTranslation.getTextOffset(brailleOffset);
    return brailleOffset;
  }

  public final int findFirstBrailleOffset (int textOffset) {
    int braille = getBrailleOffset(textOffset);
    int text = getLineOffset(braille);

    while (braille > 0) {
      int next = braille - 1;
      if (getLineOffset(next) != text) break;
      braille = next;
    }

    return braille;
  }

  public final int findLastBrailleOffset (int textOffset) {
    int braille = getBrailleOffset(textOffset);
    int text = getLineOffset(braille);
    int length = getBrailleLength();

    while (braille < length) {
      int next = braille + 1;
      if (getLineOffset(next) != text) break;
      braille = next;
    }

    return braille;
  }

  public final int findEndBrailleOffset (int textOffset) {
    if (textOffset == 0) return 0;
    return findLastBrailleOffset(textOffset-1) + 1;
  }

  protected boolean braille () {
    return Devices.braille.get().write(this);
  }

  public boolean write () {
    Endpoints.READ_LOCK.lock();
    try {
      if (this != Endpoints.getCurrentEndpoint()) return true;
      say();
      return braille();
    } finally {
      Endpoints.READ_LOCK.unlock();
    }
  }

  public final boolean refresh () {
    synchronized (this) {
      refreshBrailleTranslation();
      return write();
    }
  }

  public void onForeground (boolean prepare) {
    if (prepare) {
      resetSpeech();
    } else {
      setSayUnchangedLine();
    }

    refresh();
  }

  public void onBackground () {
  }

  public boolean isInputArea () {
    return false;
  }

  public CharSequence getHintText () {
    return null;
  }

  public boolean isPasswordField () {
    return false;
  }

  public boolean isBar () {
    return false;
  }

  public boolean isSlider () {
    return false;
  }

  public boolean seekNext () {
    return false;
  }

  public boolean seekPrevious () {
    return false;
  }

  public boolean replaceText (int start, int end, CharSequence text) {
    return false;
  }

  public boolean insertText (CharSequence text) {
    return replaceText(getSelectionStart(), getSelectionEnd(), text);
  }

  public final boolean insertText (char character) {
    return insertText(Character.toString(character));
  }

  public boolean deleteText (int start, int end) {
    return replaceText(start, end, "");
  }

  public final boolean deleteSelectedText () {
    if (!hasSelection()) return false;
    return deleteText(getSelectionStart(), getSelectionEnd());
  }

  private CharSequence textString;
  private boolean softEdges;

  public CharSequence getText () {
    return textString;
  }

  public int getTextLength () {
    return textString.length();
  }

  private CharSequence lineText;
  private int lineStart;
  private int lineIndent;

  public int findPreviousNewline (int offset) {
    int length = textString.length();
    if (offset > length) offset = length;

    while (offset > 0) {
      if (textString.charAt(--offset) == '\n') return offset;
    }

    return -1;
  }

  public int findNextNewline (int offset) {
    int length = textString.length();

    while (offset < length) {
      if (textString.charAt(offset) == '\n') return offset;
      offset += 1;
    }

    return -1;
  }

  public int setLine (int textOffset) {
    lineStart = findPreviousNewline(textOffset) + 1;

    int lineEnd = findNextNewline(lineStart);
    if (lineEnd == -1) lineEnd = getTextLength();

    lineText = textString.subSequence(lineStart, lineEnd);
    refreshBrailleTranslation();

    return textOffset - lineStart;
  }

  private final static int NO_COPY = -1;
  private int copyStart = NO_COPY;

  public boolean setCopyStart (int offset) {
    if (offset < 0) return false;
    if (offset >= getTextLength()) return false;

    copyStart = offset;
    return true;
  }

  public boolean setCopyEnd (int offset) {
    if (copyStart == NO_COPY) return false;
    if (offset < copyStart) return false;
    if (offset >= getTextLength()) return false;

    return Clipboard.putText(getText().subSequence(copyStart, offset+1));
  }

  protected void setText (CharSequence text, boolean stay) {
    textString = text;
    softEdges = false;
    copyStart = NO_COPY;

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

  protected void setText (CharSequence text) {
    setText(text, false);
    resetSpeech();
  }

  public final boolean hasSoftEdges () {
    return softEdges;
  }

  public final CharSequence getLineText () {
    return lineText;
  }

  public final int getLineStart () {
    return lineStart;
  }

  public final int getLineLength () {
    return lineText.length();
  }

  public final int getLineEnd () {
    return getLineStart() + getLineLength();
  }

  public final int getLineIndent () {
    return lineIndent;
  }

  public final void setLineIndent (int indent) {
    lineIndent = indent;
  }

  public final int getBrailleStart () {
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

  public static boolean isSelection (int start, int end) {
    return (start != end) && isSelected(start) && isSelected(end);
  }

  public final boolean hasSelection () {
    synchronized (this) {
      return isSelection(selectionStart, selectionEnd);
    }
  }

  public static boolean isCursor (int start, int end) {
    return (start == end) && isSelected(start);
  }

  public final boolean hasCursor () {
    synchronized (this) {
      return isCursor(selectionStart, selectionEnd);
    }
  }

  public final CharSequence getSelectedText () {
    synchronized (this) {
      if (isInputArea() && hasSelection()) {
        return textString.subSequence(selectionStart, selectionEnd);
      }
    }

    return null;
  }

  public final int getAdjustedLineOffset (int adjustment, int offset) {
    if (adjustment == 0) return offset;

    if (adjustment > 0) {
      int length = getLineLength();
      if (offset >= length) return length;
      offset = findFirstBrailleOffset(offset);
    } else {
      if (offset <= 0) return 0;
      offset = findEndBrailleOffset(offset);
    }

    offset += adjustment;

    if (offset < 0) {
      offset = 0;
    } else {
      int length = getBrailleLength();
      if (offset > length) offset = length;
    }

    offset = getLineOffset(offset);
    return offset;
  }

  public final int getAdjustedLineOffset (int adjustment) {
    return getAdjustedLineOffset(adjustment, getLineIndent());
  }

  public final int getAdjustedTextOffset (int adjustment) {
    return getLineStart() + getAdjustedLineOffset(adjustment);
  }

  public final int toLineOffset (int textOffset) {
    return textOffset - getLineStart();
  }

  public final boolean isCharacterOffset (int textOffset) {
    int lineOffset = toLineOffset(textOffset);
    return ((lineOffset >= 0) && (lineOffset < getLineLength()));
  }

  public final boolean isCursorOffset (int textOffset) {
    int lineOffset = toLineOffset(textOffset);
    return ((lineOffset >= 0) && (lineOffset <= getLineLength()));
  }

  private final static boolean isWordBreak (char character) {
    switch (character) {
      // hard-code the space character for efficiency because it's so common
      case ' ':
        return true;

      // no-break spaces aren't word break opportunities
      case CharacterUtilities.CHAR_NNBSP:
      case CharacterUtilities.CHAR_ZNBSP:
      case CharacterUtilities.CHAR_NBSP:
        return false;

      default:
        // all other space characters are word break opportunities
        return Character.isWhitespace(character);
    }
  }

  private final boolean isWordBreak (int textOffset) {
    if (!isWordBreak(getLineText().charAt(textOffset))) return false;
    if (!ApplicationSettings.LITERARY_BRAILLE) return true;
    if (!ApplicationSettings.BRAILLE_CODE.hasJoinableWords()) return true;

    // handle joined contractions - e.g. EBAE's "to" and "by" contractions
    CharSequence brailleCharacters = getBrailleCharacters();
    int brailleOffset = getBrailleOffset(textOffset);
    if (brailleOffset == brailleCharacters.length()) return true;
    return brailleCharacters.charAt(brailleOffset) == Braille.UNICODE_ROW;
  }

  protected final void adjustLeft (int offset, int keep) {
    if (offset < lineIndent) {
      lineIndent = getAdjustedLineOffset(-keep, offset);

      if (ApplicationSettings.WORD_WRAP) {
        CharSequence text = getLineText();

        if (lineIndent < text.length()) {
          if (!isWordBreak(lineIndent)) {
            while (lineIndent > 0) {
              if (isWordBreak(--lineIndent)) {
                lineIndent += 1;
                break;
              }
            }
          }
        }
      }
    }
  }

  protected final void adjustRight (int offset, int keep) {
    int lineLength = getLineLength();
    if (offset > lineLength) offset = lineLength;

    int brailleIndent = findFirstBrailleOffset(lineIndent);
    int brailleOffset = findLastBrailleOffset(offset);

    int brailleLength = Devices.braille.get().getLength();
    int nextSegment = findNextSegment(brailleLength, lineIndent);

    if (brailleOffset >= findFirstBrailleOffset(nextSegment)) {
      lineIndent = findPreviousSegment((brailleLength - keep), offset);
    }
  }

  public final void adjustScroll (int offset) {
    offset = setLine(offset);
    int keep = ApplicationParameters.BRAILLE_SCROLL_KEEP;

    adjustLeft(offset, keep);
    adjustRight(offset, keep);
  }

  protected final boolean changeSelection (int start, int end) {
    boolean hasChanged = false;

    synchronized (this) {
      if (ApplicationSettings.LOG_UPDATES) {
        Log.d(LOG_TAG, String.format(
          "selection change: [%d:%d] -> [%d:%d]",
          selectionStart, selectionEnd, start, end
        ));
      }

      if ((start != selectionStart) || (end != selectionEnd)) {
        hasChanged = true;
        selectionStart = start;
        selectionEnd = end;
      }

      if (isCursor(start, end)) {
        adjustScroll(start);
      }
    }

    return hasChanged;
  }

  protected final boolean updateSelection (int start, int end) {
    if (changeSelection(start, end)) return write();
    return true;
  }

  public final boolean clearSelection () {
    return updateSelection(NO_SELECTION, NO_SELECTION);
  }

  public boolean isSelectable (int offset) {
    return true;
  }

  public boolean setSelection (int start, int end) {
    boolean startSelected = isSelected(start);
    if (startSelected && !isSelectable(start)) return false;

    if (end != start) {
      boolean endSelected = isSelected(end);
      if (endSelected != startSelected) return false;
      if (endSelected && !isSelectable(end)) return false;
    }

    return updateSelection(start, end);
  }

  public final boolean setCursor (int offset) {
    return setSelection(offset, offset);
  }

  public final boolean rewrite (CharSequence text) {
    synchronized (this) {
      setText(text, true);
    }

    return write();
  }

  public final boolean write (CharSequence text) {
    synchronized (this) {
      setText(text);
      clearSelection();
      softEdges = true;
    }

    return write();
  }

  public final boolean write (int string) {
    return write(getString(string));
  }

  public final boolean performAction (Class<? extends Action> type) {
    return KeyEvents.performAction(type, this);
  }

  public Class<? extends Action> getMoveBackwardAction () {
    return null;
  }

  public Class<? extends Action> getMoveForwardAction () {
    return null;
  }

  public Class<? extends Action> getScrollBackwardAction () {
    return null;
  }

  public Class<? extends Action> getScrollForwardAction () {
    return null;
  }

  public Class<? extends Action> getScrollFirstAction () {
    return null;
  }

  public Class<? extends Action> getScrollLastAction () {
    return null;
  }

  public final int findPreviousSegment (int size, int end, int cursor) {
    synchronized (this) {
      CharSequence text = getLineText();

      {
        int length = text.length();
        if (end > length) end = length;
      }

      if (end == 0) return 0;
      int start = getAdjustedLineOffset(-size, end);
      if (!ApplicationSettings.WORD_WRAP) return start;
      if (start == 0) return 0;

      if (!isWordBreak(start-1)) {
        for (int index=start; index<end; index+=1) {
          if (isWordBreak(index)) {
            start = index;
            break;
          }
        }
      }

      for (int index=start; index<end; index+=1) {
        if (index == cursor) return index;
        if (!isWordBreak(index)) return index;
      }

      return start;
    }
  }

  public final int findPreviousSegment (int size, int end) {
    return findPreviousSegment(size, end, -1);
  }

  public final int findNextSegment (int size, int indent) {
    synchronized (this) {
      int segment = getAdjustedLineOffset(size, indent);
      if (!ApplicationSettings.WORD_WRAP) return segment;

      CharSequence text = getLineText();
      if (segment == text.length()) return segment;
      if (isWordBreak(segment)) return segment;

      for (int index=segment-1; index>=indent; index-=1) {
        if (isWordBreak(index)) return index + 1;
      }

      return segment;
    }
  }

  private abstract class Panner {
    protected abstract boolean moveDisplay (int size);
    protected abstract int getLeaveMessage ();
    protected abstract Class<? extends Action> getLeaveAction ();

    protected final int getCursorLocation () {
      return hasCursor()? getSelectionStart(): -1;
    }

    protected final int getCellCount () {
      return findEndBrailleOffset(getLineLength())
           - findFirstBrailleOffset(getLineIndent());
    }

    public final boolean pan () {
      int size = Devices.braille.get().getLength();
      if (size == 0) return false;

      synchronized (Endpoint.this) {
        if (moveDisplay(size)) return write();

        if (hasSoftEdges()) {
          ApplicationUtilities.message(getLeaveMessage());
          return false;
        }
      }

      return performAction(getLeaveAction());
    }
  }

  public final boolean panLeft () {
    Panner panner = new Panner() {
      @Override
      protected boolean moveDisplay (int size) {
        int indent = getLineIndent();
        int cursor = getCursorLocation();

        if (indent == 0) {
          int start = getLineStart();
          if (start == 0) return false;

          setLine(start-1);
          indent = getLineLength();
        } else {
          int length = getLineLength();
          if (indent > length) indent = length;
        }

        if (ApplicationSettings.WORD_WRAP) {
          while (indent > 0) {
            indent -= 1;

            if ((indent == cursor) || !isWordBreak(indent)) {
              indent += 1;
              break;
            }
          }
        }

        indent = findPreviousSegment(size, indent, cursor);
        setLineIndent(indent);
        return true;
      }

      @Override
      protected int getLeaveMessage () {
        return isInputArea()?
               R.string.message_start_of_input_area:
               R.string.message_start_of_text_area;
      }

      @Override
      protected Class<? extends Action> getLeaveAction () {
        return getMoveBackwardAction();
      }
    };

    return panner.pan();
  }

  public final boolean panRight () {
    Panner panner = new Panner() {
      @Override
      protected boolean moveDisplay (int size) {
        int length = getLineLength();
        int indent = getLineIndent();

        int cellCount = getCellCount();
        if (isInputArea()) cellCount += 1;
        boolean wrapToNextLine = cellCount <= size;

        if (!wrapToNextLine) {
          indent = findNextSegment(size, indent);

          if (ApplicationSettings.WORD_WRAP) {
            int cursor = getCursorLocation();

            while (indent < length) {
              if (indent == cursor) break;
              if (!isWordBreak(indent)) break;
              indent += 1;
            }

            if (indent == length) {
              if (indent != cursor) {
                wrapToNextLine = true;
              }
            }
          }
        }

        if (wrapToNextLine) {
          int offset = getLineStart() + length + 1;
          if (offset > getTextLength()) return false;

          setLine(offset);
          indent = 0;
        }

        setLineIndent(indent);
        return true;
      }

      @Override
      protected int getLeaveMessage () {
        return isInputArea()?
               R.string.message_end_of_input_area:
               R.string.message_end_of_text_area;
      }

      @Override
      protected Class<? extends Action> getLeaveAction () {
        return getMoveForwardAction();
      }
    };

    return panner.pan();
  }

  public boolean handleClick () {
    return false;
  }

  public boolean handleKeyboardKey_enter () {
    return Endpoints.setPreviousEndpoint();
  }

  public boolean handleKeyboardKey_cursorLeft () {
    return performAction(MoveLeft.class);
  }

  public boolean handleKeyboardKey_cursorRight () {
    return performAction(MoveRight.class);
  }

  public boolean handleKeyboardKey_cursorUp () {
    return performAction(MoveUp.class);
  }

  public boolean handleKeyboardKey_cursorDown () {
    return performAction(MoveDown.class);
  }

  public boolean handleKeyboardKey_pageUp () {
    return performAction(ScrollUp.class);
  }

  public boolean handleKeyboardKey_pageDown () {
    return performAction(ScrollDown.class);
  }

  public boolean handleKeyboardKey_home () {
    return performAction(ScrollLeft.class);
  }

  public boolean handleKeyboardKey_end () {
    return performAction(ScrollRight.class);
  }

  public boolean handleDotKeys (byte keys) {
    return false;
  }

  private final KeyBindings keyBindings;

  public final KeyBindings getKeyBindings () {
    return keyBindings;
  }

  public final KeyBindingMap getRootKeyBindingMap () {
    return getKeyBindings().getRootKeyBindingMap();
  }

  public int handleNavigationKeyEvent (int keyMask, boolean press) {
    return keyMask;
  }

  public boolean handleCursorKeyEvent (int keyNumber, boolean press) {
    return false;
  }

  protected final void addKeyBindings (String name) {
    keyBindings.addKeyBindings(name);
  }

  protected Endpoint (boolean defineKeyBindings) {
    super();
    setText("");

    keyBindings = new KeyBindings(this);
    if (defineKeyBindings) addKeyBindings("all");
  }
}

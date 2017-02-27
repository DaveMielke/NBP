package org.nbp.calculator;

import java.util.List;
import java.util.ArrayList;

import android.widget.TextView;

public abstract class History {
  private History () {
  }

  public static class Entry {
    private final CharSequence text;
    private final int selectionStart;
    private final int selectionEnd;

    private Entry (CharSequence text, int start, int end) {
      this.text = text;
      selectionStart = start;
      selectionEnd = end;
    }

    public final CharSequence getText () {
      return text;
    }

    public final int getSelectionStart () {
      return selectionStart;
    }

    public final int getSelectionEnd () {
      return selectionEnd;
    }
  }

  private final static List<Entry> historyEntries = new ArrayList<Entry>();
  private static int historyIndex = 0;

  public final static int getEntryCount () {
    return historyEntries.size();
  }

  public final static boolean atStart () {
    return historyIndex == 0;
  }

  public final static boolean atEnd () {
    return historyIndex == getEntryCount();
  }

  public final static boolean moveToFirstEntry () {
    if (atStart()) return false;
    historyIndex = 0;
    return true;
  }

  public final static boolean moveToPreviousEntry () {
    if (atStart()) return false;
    historyIndex -= 1;
    return true;
  }

  public final static boolean moveToNextEntry () {
    if (atEnd()) return false;
    historyIndex += 1;
    return true;
  }

  public final static boolean moveToLastEntry () {
    if (atEnd()) return false;
    historyIndex = getEntryCount();
    return true;
  }


  public final static Entry getEntryAt (int index) {
    return historyEntries.get(index);
  }

  public final static Entry getCurrentEntry () {
    return getEntryAt(historyIndex);
  }

  private static Entry lastEntry = null;

  public final static Entry getLastEntry () {
    return lastEntry;
  }

  public final static void setLastEntry (TextView view) {
    lastEntry = new Entry(
      view.getText(), view.getSelectionStart(), view.getSelectionEnd()
    );
  }

  public final static void addLastEntry () {
    if (lastEntry != null) {
      historyEntries.add(lastEntry);
      historyIndex = getEntryCount();
      lastEntry = null;
    }
  }
}

package org.nbp.editor;

import java.util.Date;

import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;

public abstract class EditorSpan implements DialogFinisher {
  protected EditorSpan () {
  }

  private boolean containsProtectedText = true;

  public final boolean getContainsProtectedText () {
    return containsProtectedText;
  }

  protected final void setContainsProtectedText (boolean yes) {
    containsProtectedText = yes;
  }

  private final static void joinRevisions (Editable content) {
    int length = content.length();
    int start = 0;

    RevisionSpan previousRevision = null;
    final int NO_POSITION = -1;
    int previousStart = NO_POSITION;
    int previousEnd = NO_POSITION;

    while (start != length) {
      int end = content.nextSpanTransition(start, length, RevisionSpan.class);
      RevisionSpan[] nextRevisions = content.getSpans(start, end, RevisionSpan.class);

      if ((nextRevisions != null) && (nextRevisions.length == 1)) {
        RevisionSpan nextRevision = nextRevisions[0];
        int nextStart = content.getSpanStart(nextRevision);
        int nextEnd = content.getSpanEnd(nextRevision);
        boolean isJoinable = false;

      TEST_JOINABILITY:
        {
          if (nextStart != previousEnd) break TEST_JOINABILITY;
          if (nextRevision.getClass() != previousRevision.getClass()) break TEST_JOINABILITY;

          {
            String name = nextRevision.getName();
            if (name == null) break TEST_JOINABILITY;
            if (!name.equals(previousRevision.getName())) break TEST_JOINABILITY;
          }

          {
            Date nextTimestamp = nextRevision.getTimestamp();
            if (nextTimestamp == null) break TEST_JOINABILITY;

            Date previousTimestamp = previousRevision.getTimestamp();
            if (previousTimestamp == null) break TEST_JOINABILITY;

            long timeDifference = nextTimestamp.getTime() - previousTimestamp.getTime();
            if (Math.abs(timeDifference) > 600000) break TEST_JOINABILITY;
          }

          isJoinable = true;
        }

        if (isJoinable) {
          {
            Date timestamp = nextRevision.getTimestamp();

            if (previousRevision.getTimestamp().compareTo(timestamp) < 0) {
              previousRevision.setTimestamp(timestamp);
            }
          }

          content.removeSpan(nextRevision);
          content.setSpan(
            previousRevision, previousStart, nextEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
          );

          {
            int from = previousEnd;
            int to = nextStart;

            {
              CharSequence decoration = previousRevision.getDecorationSuffix();
              if (decoration != null) from -= decoration.length();
            }

            {
              CharSequence decoration = nextRevision.getDecorationPrefix();
              if (decoration != null) to += decoration.length();
            }

            if (from != to) content.delete(from, to);
          }

          previousEnd = content.getSpanEnd(previousRevision);
        } else {
          previousRevision = nextRevision;
          previousStart = nextStart;
          previousEnd = nextEnd;
        }
      } else {
        previousRevision = null;
        previousStart = NO_POSITION;
        previousEnd = NO_POSITION;
      }

      start = end;
    }
  }

  protected void finishSpan (Editable content) {
  }

  public final static void finishSpans (Editable content) {
    for (EditorSpan span : content.getSpans(0, content.length(), EditorSpan.class)) {
      span.finishSpan(content);
    }

    joinRevisions(content);
  }

  public void restoreSpan (Spannable content) {
  }

  @Override
  public void finishDialog (DialogHelper helper) {
  }
}

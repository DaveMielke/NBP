package org.nbp.editor;
import org.nbp.editor.spans.RevisionSpan;
import org.nbp.editor.spans.InsertionSpan;
import org.nbp.editor.spans.DeletionSpan;

import java.util.Date;

import android.text.Editable;
import android.text.Spanned;

public abstract class Revisions {
  private Revisions () {
  }

  private final static boolean isSameReviewer (RevisionSpan revision1, RevisionSpan revision2) {
    String name = revision1.getReviewerName();
    if (name == null) return false;
    return name.equals(revision2.getReviewerName());
  }

  private final static boolean canJoinRevisions (RevisionSpan revision1, RevisionSpan revision2) {
    if (revision1.getClass() != revision2.getClass()) return false;
    if (!isSameReviewer(revision1, revision2)) return false;

    {
      Date time1 = revision1.getReviewTime();
      if (time1 == null) return false;

      Date time2 = revision2.getReviewTime();
      if (time2 == null) return false;

      long timeDifference = time2.getTime() - time1.getTime();
      if (Math.abs(timeDifference) > ApplicationParameters.REVISION_JOIN_MILLISECONDS) return false;
    }

    if (revision1 instanceof DeletionSpan) {
      DeletionSpan deletion1 = (DeletionSpan)revision1;
      DeletionSpan deletion2 = (DeletionSpan)revision2;

      InsertionSpan insertion1 = deletion1.getInsertion();
      InsertionSpan insertion2 = deletion2.getInsertion();

      boolean haveInsertion1 = insertion1 != null;
      boolean haveInsertion2 = insertion2 != null;

      if (haveInsertion1 || haveInsertion2) {
        if (!haveInsertion1) return false;
        if (!haveInsertion2) return false;
        if (!isSameReviewer(insertion1, insertion2)) return false;
      }
    }

    return true;
  }

  public final static void joinRevisions (Editable content) {
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
        boolean join = false;

        if (nextStart == previousEnd) {
          if (canJoinRevisions(previousRevision, nextRevision)) {
            join = true;
          }
        }

        if (join) {
          {
            Date time = nextRevision.getReviewTime();

            if (previousRevision.getReviewTime().compareTo(time) < 0) {
              previousRevision.setReviewTime(time);
            }
          }

          content.removeSpan(nextRevision);
          content.setSpan(
            previousRevision, previousStart, nextEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
          );

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
}

package org.nbp.editor;

import java.util.Date;

import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

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

  public int getPosition (Spanned content) {
    return content.getSpanStart(this);
  }

  public void finishSpan (Editable content) {
  }

  public final static void finishSpans (Editable content) {
    Revisions.joinRevisions(content);

    for (EditorSpan span : content.getSpans(0, content.length(), EditorSpan.class)) {
      span.finishSpan(content);
    }
  }

  public void restoreSpan (Spannable content) {
  }

  @Override
  public void finishDialog (DialogHelper helper) {
  }
}

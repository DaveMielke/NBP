package org.nbp.editor;

import android.text.Editable;
import android.text.Spannable;

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

  protected void finishSpan (Editable content) {
  }

  public final static void finishSpans (Editable content) {
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

package org.nbp.editor;

import android.text.Editable;
import android.text.Spannable;

public abstract class EditorSpan implements DialogFinisher {
  protected EditorSpan () {
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

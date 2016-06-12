package org.nbp.editor;

import android.text.style.StrikethroughSpan;

public class DeleteSpan extends RevisionSpan {
  @Override
  public final String getSpanIdentifier () {
    return "del";
  }

  public DeleteSpan (CharSequence text) {
    super(text, new StrikethroughSpan());
  }
}

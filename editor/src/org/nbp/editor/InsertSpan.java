package org.nbp.editor;

import android.text.style.UnderlineSpan;

public class InsertSpan extends RevisionSpan {
  @Override
  public final String getSpanIdentifier () {
    return "ins";
  }

  public InsertSpan (CharSequence text) {
    super(text, new UnderlineSpan());
  }
}

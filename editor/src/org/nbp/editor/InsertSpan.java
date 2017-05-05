package org.nbp.editor;

import android.text.style.UnderlineSpan;

public class InsertSpan extends RevisionSpan {
  private final static String decorationPrefix = "⣏⢎";
  private final static String decorationSuffix = "⡱⣹";

  public InsertSpan (CharSequence text) {
    super(text, decorationPrefix, decorationSuffix);
  }

  @Override
  public final String getSpanIdentifier () {
    return "ins";
  }
}

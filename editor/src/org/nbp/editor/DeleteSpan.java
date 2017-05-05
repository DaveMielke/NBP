package org.nbp.editor;

import android.text.style.StrikethroughSpan;

public class DeleteSpan extends RevisionSpan {
  private final static String decorationPrefix = "⣏⡱";
  private final static String decorationSuffix = "⢎⣹";

  public DeleteSpan (CharSequence text) {
    super(text, decorationPrefix, decorationSuffix);
  }

  @Override
  public final String getSpanIdentifier () {
    return "del";
  }
}

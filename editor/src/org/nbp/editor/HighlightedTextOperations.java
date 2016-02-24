package org.nbp.editor;

import android.text.Html;
import android.text.SpannableStringBuilder;

public class HighlightedTextOperations extends TextOperations {
  @Override
  protected void postProcessInput (SpannableStringBuilder content) {
    content.replace(0, content.length(), Html.fromHtml(content.toString()));
  }

  @Override
  protected String preprocessOutput (CharSequence content) {
    return Html.toHtml(asSpanned(content));
  }

  public HighlightedTextOperations () {
    super();
  }
}

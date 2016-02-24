package org.nbp.editor;

import android.text.Html;
import android.text.SpannableStringBuilder;

public class HighlightedTextOperations extends TextOperations {
  @Override
  protected void postProcessInput (SpannableStringBuilder input) {
    input.replace(0, input.length(), Html.fromHtml(input.toString()));
  }

  @Override
  protected String preprocessOutput (CharSequence output) {
    return Html.toHtml(asSpanned(output));
  }

  public HighlightedTextOperations () {
    super();
  }
}

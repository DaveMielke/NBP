package org.nbp.editor;

import android.text.Html;
import android.text.SpannableStringBuilder;

public class HtmlFileHandler extends TextFileHandler {
  @Override
  protected void postProcessInput (SpannableStringBuilder input) {
    input.replace(0, input.length(), Html.fromHtml(input.toString()));
  }

  @Override
  protected String preprocessOutput (CharSequence output) {
    return Html.toHtml(asSpanned(output));
  }

  public HtmlFileHandler () {
    super();
  }
}

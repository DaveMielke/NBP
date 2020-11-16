package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;
import org.nbp.editor.spans.FontSpan;

import android.text.Spannable;
import com.aspose.words.*;

public class WordsFontSpan extends FontSpan {
  private final Font wordsFont;

  public WordsFontSpan (Font font, Spannable content, int start) {
    super(content, start);
    wordsFont = font;
  }

  public final Font getFont () {
    return wordsFont;
  }

  @Override
  protected boolean isBold () {
    try {
      return wordsFont.getBold();
    } catch (Exception exception) {
    }

    return false;
  }

  @Override
  protected boolean isItalic () {
    try {
      return wordsFont.getItalic();
    } catch (Exception exception) {
    }

    return false;
  }

  @Override
  protected boolean isUnderline () {
    try {
      return wordsFont.getUnderline() != Underline.NONE;
    } catch (Exception exception) {
    }

    return false;
  }

  @Override
  protected boolean isStrikeThrough () {
    try {
      return wordsFont.getStrikeThrough();
    } catch (Exception exception) {
    }

    return false;
  }

  @Override
  protected boolean isSuperscript () {
    try {
      return wordsFont.getSuperscript();
    } catch (Exception exception) {
    }

    return false;
  }

  @Override
  protected boolean isSubscript () {
    try {
      return wordsFont.getSubscript();
    } catch (Exception exception) {
    }

    return false;
  }
}

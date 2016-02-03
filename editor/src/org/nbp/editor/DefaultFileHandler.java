package org.nbp.editor;

import java.io.File;

import org.nbp.common.InputProcessor;

import android.text.SpannableStringBuilder;

public class DefaultFileHandler extends FileHandler {
  @Override
  public final void read (File file, final SpannableStringBuilder sb) {
    new InputProcessor() {
      @Override
      protected final boolean handleLine (CharSequence text, int number) {
        if (sb.length() > 0) sb.append('\n');
        sb.append(text);
        return true;
      }
    }.processInput(file);
  }

  @Override
  public final void write (File file, SpannableStringBuilder sb) {
  }

  public DefaultFileHandler () {
    super();
  }
}

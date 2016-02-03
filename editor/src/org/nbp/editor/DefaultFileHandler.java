package org.nbp.editor;

import java.io.File;

import org.nbp.common.InputProcessor;
import android.text.SpannableStringBuilder;

import org.nbp.common.FileMaker;
import java.io.Writer;
import java.io.IOException;

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
  public final void write (File file, final CharSequence content) {
    new FileMaker() {
      @Override
      protected final boolean writeContent (Writer writer) throws IOException {
        writer.write(content.toString());
        writer.write('\n');
        return true;
      }
    };
  }

  public DefaultFileHandler () {
    super();
  }
}

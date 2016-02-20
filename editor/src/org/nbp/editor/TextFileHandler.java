package org.nbp.editor;

import org.nbp.common.CommonContext;
import org.nbp.common.CommonUtilities;
import java.io.File;

import org.nbp.common.InputProcessor;
import android.text.SpannableStringBuilder;

import org.nbp.common.FileMaker;
import java.io.Writer;
import java.io.IOException;

public class TextFileHandler extends FileHandler {
  private final static String LOG_TAG = TextFileHandler.class.getName();

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
    String path = file.getAbsolutePath();
    String newPath = path + ".new";
    File newFile = new File(newPath);
    newFile.delete();

    FileMaker fileMaker = new FileMaker() {
      @Override
      protected final boolean writeContent (Writer writer) throws IOException {
        writer.write(content.toString());
        writer.write('\n');
        return true;
      }
    };

    if (fileMaker.makeFile(newFile) != null) {
      if (!newFile.renameTo(file)) {
        CommonUtilities.reportError(
          LOG_TAG, "%s: %s -> %s",
          CommonContext.getString(R.string.alert_rename_failed),
          newFile.getAbsolutePath(), file.getAbsolutePath()
        );
      }
    }
  }

  public TextFileHandler () {
    super();
  }
}

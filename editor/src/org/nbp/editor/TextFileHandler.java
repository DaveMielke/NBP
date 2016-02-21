package org.nbp.editor;

import java.io.IOException;
import java.io.File;
import java.io.Writer;

import org.nbp.common.CommonContext;
import org.nbp.common.CommonUtilities;

import org.nbp.common.InputProcessor;
import org.nbp.common.FileMaker;

import android.text.Spanned;
import android.text.SpannedString;
import android.text.SpannableStringBuilder;

public class TextFileHandler extends FileHandler {
  private final static String LOG_TAG = TextFileHandler.class.getName();

  protected void postProcessInput (SpannableStringBuilder input) {
  }

  protected String preprocessOutput (CharSequence output) {
    return output.toString();
  }

  @Override
  public final void read (File file, final SpannableStringBuilder input) {
    new InputProcessor() {
      @Override
      protected final boolean handleLine (CharSequence text, int number) {
        if (input.length() > 0) input.append('\n');
        input.append(text);
        return true;
      }
    }.processInput(file);

    postProcessInput(input);
  }

  protected final Spanned asSpanned (CharSequence string) {
    if (string instanceof Spanned) return (Spanned)string;
    return new SpannedString(string);
  }

  @Override
  public final void write (File file, final CharSequence output) {
    String path = file.getAbsolutePath();
    String newPath = path + ".new";
    File newFile = new File(newPath);
    newFile.delete();

    FileMaker fileMaker = new FileMaker() {
      @Override
      protected final boolean writeContent (Writer writer) throws IOException {
        writer.write(preprocessOutput(output));
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

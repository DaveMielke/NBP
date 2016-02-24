package org.nbp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Writer;
import java.io.OutputStreamWriter;

import org.nbp.common.CommonContext;
import org.nbp.common.CommonUtilities;
import org.nbp.common.InputProcessor;

import android.text.Spanned;
import android.text.SpannedString;
import android.text.SpannableStringBuilder;

public class TextOperations implements ContentOperations {
  private final static String LOG_TAG = TextOperations.class.getName();

  protected final Spanned asSpanned (CharSequence string) {
    if (string instanceof Spanned) return (Spanned)string;
    return new SpannedString(string);
  }

  protected void postProcessInput (SpannableStringBuilder content) {
  }

  protected String preprocessOutput (CharSequence content) {
    return content.toString();
  }

  @Override
  public final boolean read (InputStream stream, final SpannableStringBuilder content) {
    InputProcessor inputProcessor = new InputProcessor() {
      @Override
      protected final boolean handleLine (CharSequence text, int number) {
        if (content.length() > 0) content.append('\n');
        content.append(text);
        return true;
      }
    };

    if (inputProcessor.processInput(stream)) {
      postProcessInput(content);
      return true;
    }

    return false;
  }

  @Override
  public final boolean write (OutputStream stream, CharSequence content) {
    try {
      Writer writer = new OutputStreamWriter(stream);
      writer.write(preprocessOutput(content));
      writer.write('\n');
      return true;
    } catch (IOException exception) {
      CommonUtilities.reportError(
        LOG_TAG, "content write error: %s",
        exception.getMessage()
      );
    }

    return false;
  }

  public TextOperations () {
    super();
  }
}

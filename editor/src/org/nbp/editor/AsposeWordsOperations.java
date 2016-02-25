package org.nbp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import org.nbp.common.CommonContext;
import android.content.Context;

import android.text.Spanned;
import android.text.SpannedString;
import android.text.SpannableStringBuilder;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;

import com.aspose.words.*;

public class AsposeWordsOperations extends AsposeWordsApplication implements ContentOperations {
  private final static String LOG_TAG = AsposeWordsOperations.class.getName();

  private final static License license = new License();
  private static Throwable licenseProblem = null;

  public AsposeWordsOperations () throws IOException {
    super();

    synchronized (LOG_TAG) {
      if (licenseProblem == null) {
        Context context = CommonContext.getContext();
        loadLibs(context);

        try {
          license.setLicense(context.getAssets().open("Aspose.Words.lic"));
          Log.d(LOG_TAG, "Aspose Words ready");
          return;
        } catch (Throwable problem) {
          licenseProblem = problem;
        }

        Log.w(LOG_TAG, ("Aspose Words license problem: " + licenseProblem.getMessage()));
        throw new IOException("Aspose Words license problem", licenseProblem);
      }
    }
  }

  @Override
  public final void read (InputStream stream, SpannableStringBuilder content) throws IOException {
    try {
      Document document = new Document(stream);

      for (Object documentChild : document.getFirstSection().getBody().getChildNodes()) {
        if (documentChild instanceof Paragraph) {
          Paragraph paragraph = (Paragraph)documentChild;

          for (Object paragraphChild : paragraph.getChildNodes()) {
            if (paragraphChild instanceof Run) {
              Run run = (Run)paragraphChild;

              CharSequence text = run.getText();
              Font font = run.getFont();

              int start = content.length();
              content.append(text);
              int end = content.length();
              HighlightSpans.Entry spanEntry = null;

              if (font.getBold() && font.getItalic()) {
                spanEntry = HighlightSpans.BOLD_ITALIC;
              } else if (font.getBold()) {
                spanEntry = HighlightSpans.BOLD;
              } else if (font.getItalic()) {
                spanEntry = HighlightSpans.ITALIC;
              } else if (font.getUnderline() != Underline.NONE) {
                spanEntry = HighlightSpans.UNDERLINE;
              }

              if (spanEntry != null) {
                content.setSpan(spanEntry.newInstance(), start, end, content.SPAN_EXCLUSIVE_EXCLUSIVE);
              }
            }
          }

          content.append('\n');
        }
      }
    } catch (Exception exception) {
      throw new IOException("Aspose Words input error", exception);
    }
  }

  @Override
  public final void write (OutputStream stream, CharSequence content) throws IOException {
    try {
      DocumentBuilder builder = new DocumentBuilder();

      Spanned text = (content instanceof Spanned)? (Spanned)content: new SpannedString(content);
      int length = text.length();
      int start = 0;

      while (start < length) {
        int end = text.nextSpanTransition(start, length, CharacterStyle.class);

        Font font = builder.getFont();
        font.clearFormatting();

        for (CharacterStyle span : text.getSpans(start, end, CharacterStyle.class)) {
          if (HighlightSpans.BOLD_ITALIC.isFor(span)) {
            font.setBold(true);
            font.setItalic(true);
          } else if (HighlightSpans.BOLD.isFor(span)) {
            font.setBold(true);
          } else if (HighlightSpans.ITALIC.isFor(span)) {
            font.setItalic(true);
          } else if (HighlightSpans.UNDERLINE.isFor(span)) {
            font.setUnderline(Underline.DASH);
          }
        }

        builder.write(text.subSequence(start, end).toString());
        start = end;
      }

      builder.getDocument().save(null);
    } catch (Exception exception) {
      throw new IOException("Aspose Words output error", exception);
    }
  }
}

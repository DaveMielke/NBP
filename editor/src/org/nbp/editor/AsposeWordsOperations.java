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

public class AsposeWordsOperations implements ContentOperations {
  private final static String LOG_TAG = AsposeWordsOperations.class.getName();

  private final static AsposeWordsApplication application = new AsposeWordsApplication();
  private final static License license = new License();
  private static Throwable licenseProblem;

  static {
    Context context = CommonContext.getContext();
    application.loadLibs(context);

    try {
      license.setLicense(context.getAssets().open("Aspose.Words.lic"));
      Log.d(LOG_TAG, "Aspose Words ready");
      licenseProblem = null;
    } catch (Throwable problem) {
      Log.w(LOG_TAG, ("Aspose Words license problem: " + problem.getMessage()));
      licenseProblem = problem;
    }
  }

  private static void checkForLicenseProblem () throws IOException {
    if (licenseProblem != null) {
      throw new IOException("Aspose Words license problem", licenseProblem);
    }
  }

  private final int saveFormat;
  private final int loadFormat;

  public AsposeWordsOperations (int saveFormat, int loadFormat) throws IOException {
    super();
    this.saveFormat = saveFormat;
    this.loadFormat = loadFormat;
  }

  public AsposeWordsOperations (int saveFormat) throws IOException {
    this(saveFormat, LoadFormat.UNKNOWN);
  }

  private final void logUnhandledChildNode (Node parent, Object child) {
    if (false) {
      Log.d(LOG_TAG, String.format(
        "unhandled child node: %s contains %s",
        parent.getClass().getSimpleName(),
        child.getClass().getSimpleName()
      ));
    }
  }

  private final void addSpan (SpannableStringBuilder content, int start, Object span) {
    content.setSpan(span, start, content.length(), content.SPAN_EXCLUSIVE_EXCLUSIVE);
  }

  private final void addSpan (SpannableStringBuilder content, int start, HighlightSpans.Entry spanEntry) {
    addSpan(content, start, spanEntry.newInstance());
  }

  private final void addRun (SpannableStringBuilder content, Run run) throws Exception {
    final int start = content.length();
    content.append(run.getText());

    if (run.isInsertRevision()) {
      addSpan(content, start, new InsertSpan());
    }

    if (run.isDeleteRevision()) {
      addSpan(content, start, new DeleteSpan());
    }

    {
      Font font = run.getFont();

      if (font.getBold()) {
        addSpan(content, start,
                font.getItalic()? HighlightSpans.BOLD_ITALIC: HighlightSpans.BOLD);
      }

      if (font.getItalic()) {
        addSpan(content, start, HighlightSpans.ITALIC);
      }

      if (font.getStrikeThrough()) {
        addSpan(content, start, HighlightSpans.STRIKE);
      }

      if (font.getSubscript()) {
        addSpan(content, start, HighlightSpans.SUBSCRIPT);
      }

      if (font.getSuperscript()) {
        addSpan(content, start, HighlightSpans.SUPERSCRIPT);
      }

      if (font.getUnderline() != Underline.NONE) {
        addSpan(content, start, HighlightSpans.UNDERLINE);
      }
    }
  }

  private final void addParagraph (SpannableStringBuilder content, Paragraph paragraph) throws Exception {
    int start = content.length();

    for (Object child : paragraph.getChildNodes()) {
      if (child instanceof Run) {
        Run run = (Run)child;
        addRun(content, run);
      } else {
        logUnhandledChildNode(paragraph, child);
      }
    }

    {
      int length = content.length();

      if (length > 0) {
        if (content.charAt(length-1) != '\n') {
          content.append('\n');
        }
      }
    }

    addSpan(content, start, new ParagraphSpan());
  }

  private final void addSection (SpannableStringBuilder content, Section section) throws Exception {
    int start = content.length();

    for (Object child : section.getBody().getChildNodes()) {
      if (child instanceof Paragraph) {
        Paragraph paragraph = (Paragraph)child;
        addParagraph(content, paragraph);
      } else {
        logUnhandledChildNode(section, child);
      }
    }

    addSpan(content, start, new SectionSpan());
  }

  @Override
  public final void read (InputStream stream, SpannableStringBuilder content) throws IOException {
    checkForLicenseProblem();

    if (loadFormat == LoadFormat.UNKNOWN) {
      throw new IOException("reading not supported");
    }

    try {
      LoadOptions options = new LoadOptions();
      options.setLoadFormat(loadFormat);

      Document document = new Document(stream, options);
      addSection(content, document.getFirstSection());
    } catch (Exception exception) {
      throw new IOException("Aspose Words input error", exception);
    }
  }

  @Override
  public final void write (OutputStream stream, CharSequence content) throws IOException {
    checkForLicenseProblem();

    if (saveFormat == SaveFormat.UNKNOWN) {
      throw new IOException("writing not supported");
    }

    try {
      DocumentBuilder builder = new DocumentBuilder();

      Spanned text = (content instanceof Spanned)? (Spanned)content: new SpannedString(content);
      int length = text.length();
      int start = 0;

      while (start < length) {
        Font font = builder.getFont();
        font.clearFormatting();

        int end = text.nextSpanTransition(start, length, CharacterStyle.class);
        CharacterStyle[] spans = text.getSpans(start, end, CharacterStyle.class);

        if (spans != null) {
          for (CharacterStyle span : spans) {
            if (HighlightSpans.BOLD_ITALIC.isFor(span)) {
              font.setBold(true);
              font.setItalic(true);
            } else if (HighlightSpans.BOLD.isFor(span)) {
              font.setBold(true);
            } else if (HighlightSpans.ITALIC.isFor(span)) {
              font.setItalic(true);
            } else if (HighlightSpans.STRIKE.isFor(span)) {
              font.setStrikeThrough(true);
            } else if (HighlightSpans.SUBSCRIPT.isFor(span)) {
              font.setSubscript(true);
            } else if (HighlightSpans.SUPERSCRIPT.isFor(span)) {
              font.setSuperscript(true);
            } else if (HighlightSpans.UNDERLINE.isFor(span)) {
              font.setUnderline(Underline.DASH);
            }
          }
        }

        builder.write(text.subSequence(start, end).toString());
        start = end;
      }

      builder.getDocument().save(stream, saveFormat);
    } catch (Exception exception) {
      throw new IOException("Aspose Words output error", exception);
    }
  }
}

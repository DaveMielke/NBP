package org.nbp.editor;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import android.content.Context;

import android.text.Spanned;
import android.text.SpannedString;
import android.text.SpannableStringBuilder;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;

import com.aspose.words.*;

public class AsposeWordsOperations extends ContentOperations {
  private final static String LOG_TAG = AsposeWordsOperations.class.getName();

  private final static AsposeWordsLicense asposeLicense = AsposeWordsLicense.singleton();

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

  private final static Map<String, String> listLabelMap =
               new HashMap<String, String>();

  static {
    listLabelMap.put("\uF0B7", "\u2022");
  }

  private class ContentReader {
    private final Map<Node, Revision> nodeRevisionMap =
                 new HashMap<Node, Revision>();

    private final void mapRevisions (Document document) {
      RevisionCollection revisions = document.getRevisions();

      if (revisions != null) {
        for (Revision revision : revisions) {
          Node node = revision.getParentNode();
          if (node != null) nodeRevisionMap.put(node, revision);
        }
      }
    }

    private final void addRevisionSpan (
      SpannableStringBuilder content, int start,
      RevisionSpan span, Node node
    ) {
      if (addSpan(content, start, span)) {
        Revision revision = nodeRevisionMap.get(node);

        if (revision != null) {
          span.setAuthor(revision.getAuthor());
          span.setTimestamp(revision.getDateTime());
          span.decorateText(content);
        }
      }
    }

    private final void logUnhandledChildNode (Node parent, Object child) {
      if (ApplicationParameters.ASPOSE_LOG_UNHANDLED_CHILDREN) {
        Log.d(LOG_TAG, String.format(
          "unhandled child node: %s contains %s",
          parent.getClass().getSimpleName(),
          child.getClass().getSimpleName()
        ));
      }
    }

    private final void addRun (SpannableStringBuilder content, Run run) throws Exception {
      final int start = content.length();
      content.append(run.getText());

      if (run.isInsertRevision()) {
        addRevisionSpan(content, start, new InsertSpan(), run);
      }

      if (run.isDeleteRevision()) {
        addRevisionSpan(content, start, new DeleteSpan(), run);
      }

      {
        Font font = run.getFont();

        if (font.getBold()) {
          addSpan(content, start,
                  font.getItalic()?
                    HighlightSpans.BOLD_ITALIC:
                    HighlightSpans.BOLD);
        } else if (font.getItalic()) {
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

      if (paragraph.isListItem()) {
        ListLabel label = paragraph.getListLabel();
        String string = label.getLabelString();

        String mapped = listLabelMap.get(string);
        if (mapped != null) string = mapped;

        content.append(String.format("[%s] ", string));
      }

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

      if (paragraph.isInsertRevision()) {
        addRevisionSpan(content, start, new InsertSpan(), paragraph);
      }

      if (paragraph.isDeleteRevision()) {
        addRevisionSpan(content, start, new DeleteSpan(), paragraph);
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

    public ContentReader (InputStream stream, SpannableStringBuilder content) throws Exception {
      LoadOptions options = new LoadOptions();
      options.setLoadFormat(loadFormat);

      Document document = new Document(stream, options);
      document.updateListLabels();

      mapRevisions(document);
      addSection(content, document.getFirstSection());
    }
  }

  @Override
  public final void read (InputStream stream, SpannableStringBuilder content) throws IOException {
    asposeLicense.check();
    if (loadFormat == LoadFormat.UNKNOWN) readingNotSupported();

    try {
      new ContentReader(stream, content);
    } catch (Exception exception) {
      Log.w(LOG_TAG, ("input error: " + exception.getMessage()));
      throw new IOException("Aspose Words input error", exception);
    }
  }

  private class ContentWriter {
    private abstract class RevisionFinisher {
      public abstract void finishRevision () throws Exception;
    }

    private final void beginRevisionTracking (DocumentBuilder builder, RevisionSpan span) {
      builder.getDocument().startTrackRevisions(
        span.getAuthor(), span.getTimestamp()
      );
    }

    private final void endRevisionTracking (DocumentBuilder builder) {
      builder.getDocument().stopTrackRevisions();
    }

    private int bookmarkNumber = 0;

    private final String newBookmarkName () {
      return "bookmark" + ++bookmarkNumber;
    }

    private final BookmarkStart beginBookmark (DocumentBuilder builder, String name) throws Exception {
      return builder.startBookmark(name);
    }

    private final void endBookmark (DocumentBuilder builder, String name) throws Exception {
      builder.endBookmark(name);
    }

    private final RevisionFinisher beginInsertRevision (
      final DocumentBuilder builder,
      final RevisionSpan span
    ) {
      beginRevisionTracking(builder, span);

      return new RevisionFinisher() {
        @Override
        public void finishRevision () {
          endRevisionTracking(builder);
        }
      };
    }

    private final RevisionFinisher beginDeleteRevision (
      final DocumentBuilder builder,
      final RevisionSpan span
    ) throws Exception {
      final String bookmarkName = newBookmarkName();
      final BookmarkStart bookmarkStart = beginBookmark(builder, bookmarkName);

      return new RevisionFinisher() {
        @Override
        public void finishRevision () throws Exception {
          endBookmark(builder, bookmarkName);
          beginRevisionTracking(builder, span);
          bookmarkStart.getBookmark().setText("");
          endRevisionTracking(builder);
        }
      };
    }

    private final RevisionFinisher beginRevision (
      DocumentBuilder builder,
      RevisionSpan span
    ) throws Exception {
      if (span == null) return null;
      if (span instanceof InsertSpan) return beginInsertRevision(builder, span);
      if (span instanceof DeleteSpan) return beginDeleteRevision(builder, span);
      return null;
    }

    public ContentWriter (OutputStream stream, CharSequence content) throws Exception {
      DocumentBuilder builder = new DocumentBuilder();

      Spanned text = (content instanceof Spanned)? (Spanned)content: new SpannedString(content);
      int length = text.length();
      int start = 0;

      RevisionSpan oldRevisionSpan = null;
      RevisionFinisher revisionFinisher = null;

      while (start < length) {
        boolean isDecoration = false;
        RevisionSpan newRevisionSpan = null;

        Font font = builder.getFont();
        font.clearFormatting();

        int end = text.nextSpanTransition(start, length, Object.class);
        Object[] spans = text.getSpans(start, end, Object.class);

        if (spans != null) {
          for (Object span : spans) {
            if (span instanceof CharacterStyle) {
              CharacterStyle characterStyle = (CharacterStyle)span;

              if (HighlightSpans.BOLD_ITALIC.isFor(characterStyle)) {
                font.setBold(true);
                font.setItalic(true);
              } else if (HighlightSpans.BOLD.isFor(characterStyle)) {
                font.setBold(true);
              } else if (HighlightSpans.ITALIC.isFor(characterStyle)) {
                font.setItalic(true);
              } else if (HighlightSpans.STRIKE.isFor(characterStyle)) {
                font.setStrikeThrough(true);
              } else if (HighlightSpans.SUBSCRIPT.isFor(characterStyle)) {
                font.setSubscript(true);
              } else if (HighlightSpans.SUPERSCRIPT.isFor(characterStyle)) {
                font.setSuperscript(true);
              } else if (HighlightSpans.UNDERLINE.isFor(characterStyle)) {
                font.setUnderline(Underline.DASH);
              }
            } else if (span instanceof RevisionSpan) {
              newRevisionSpan = (RevisionSpan)span;
            } else if (span instanceof DecorationSpan) {
              isDecoration = true;
            }
          }
        }

        if (newRevisionSpan != oldRevisionSpan) {
          if (revisionFinisher != null) {
            revisionFinisher.finishRevision();
            revisionFinisher = null;
          }

          oldRevisionSpan = newRevisionSpan;
          revisionFinisher = beginRevision(builder, newRevisionSpan);
        }

        if (!isDecoration) {
          builder.write(text.subSequence(start, end).toString());
        }

        start = end;
      }

      if (revisionFinisher != null) revisionFinisher.finishRevision();
      builder.getDocument().save(stream, saveFormat);
    }
  }

  @Override
  public final void write (OutputStream stream, CharSequence content) throws IOException {
    asposeLicense.check();
    if (saveFormat == SaveFormat.UNKNOWN) writingNotSupported();

    try {
      new ContentWriter(stream, content);
    } catch (Exception exception) {
      Log.w(LOG_TAG, ("output error: " + exception.getMessage()));
      throw new IOException("Aspose Words output error", exception);
    }
  }
}

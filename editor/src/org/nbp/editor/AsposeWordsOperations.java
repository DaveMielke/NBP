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

import android.text.Editable;
import android.text.Spannable;
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
      Editable content, int start,
      RevisionSpan span, Node node
    ) {
      if (addSpan(content, start, span)) {
        Revision revision = nodeRevisionMap.get(node);

        if (revision != null) {
          span.setAuthor(revision.getAuthor());
          span.setTimestamp(revision.getDateTime());
        }
      }
    }

    private class CommentDescriptor {
      public CommentDescriptor () {
      }

      private final void setSpan (Spannable content, Object span) {
        int position = content.length();
        content.setSpan(span, position, position, content.SPAN_MARK_MARK);
      }

      private Comment commentNode = null;
      private CommentRangeStart startPosition = null;
      private CommentRangeEnd endPosition = null;

      public final  Comment getComment () {
        return commentNode;
      }

      public final void setComment (Spannable content, Comment comment) {
        commentNode = comment;
        setSpan(content, comment);
      }

      public final CommentRangeStart getStart () {
        return startPosition;
      }

      public final void setStart (Spannable content, CommentRangeStart start) {
        startPosition = start;
        setSpan(content, start);
      }

      public final CommentRangeEnd getEnd () {
        return endPosition;
      }

      public final void setEnd (Spannable content, CommentRangeEnd end) {
        endPosition = end;
        setSpan(content, end);
      }
    }

    private final Map<Integer, CommentDescriptor> commentDescriptors =
          new HashMap<Integer, CommentDescriptor>();

    private final CommentDescriptor getCommentDescriptor (int identifier) {
      CommentDescriptor comment = commentDescriptors.get(identifier);
      if (comment != null) return comment;

      comment = new CommentDescriptor();
      commentDescriptors.put(identifier, comment);
      return comment;
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

    private final void addRun (Editable content, Run run) throws Exception {
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

    private final void addParagraph (Editable content, Paragraph paragraph) throws Exception {
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
          continue;
        }

        if (child instanceof Comment) {
          Comment comment = (Comment)child;
          getCommentDescriptor(comment.getId()).setComment(content, comment);
          continue;
        }

        if (child instanceof CommentRangeStart) {
          CommentRangeStart crs = (CommentRangeStart)child;
          getCommentDescriptor(crs.getId()).setStart(content, crs);
          continue;
        }

        if (child instanceof CommentRangeEnd) {
          CommentRangeEnd cre = (CommentRangeEnd)child;
          getCommentDescriptor(cre.getId()).setEnd(content, cre);
          continue;
        }

        logUnhandledChildNode(paragraph, child);
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

    private final void addSection (Editable content, Section section) throws Exception {
      int start = content.length();

      for (Object child : section.getBody().getChildNodes()) {
        if (child instanceof Paragraph) {
          Paragraph paragraph = (Paragraph)child;
          addParagraph(content, paragraph);
          continue;
        }

        logUnhandledChildNode(section, child);
      }

      addSpan(content, start, new SectionSpan());
    }

    private final void addComment (Editable content, Comment comment) throws Exception {
      int start = content.length();

      for (Object child : comment.getChildNodes()) {
        if (child instanceof Paragraph) {
          Paragraph paragraph = (Paragraph)child;
          addParagraph(content, paragraph);
          continue;
        }

        logUnhandledChildNode(comment, child);
      }
    }

    private final int getPosition (Spanned content, Object span) {
      Object[] spans = content.getSpans(0, content.length(), span.getClass());
      return content.getSpanStart(spans[0]);
    }

    private final void addComment (Editable content, CommentDescriptor descriptor) throws Exception {
      Comment comment = descriptor.getComment();
      if (comment == null) return;

      Node startNode = descriptor.getStart();
      if (startNode == null) startNode = comment;

      Node endNode = descriptor.getEnd();
      if (endNode == null) endNode = comment;

      int startPosition = getPosition(content, startNode);
      int endPosition = (endNode == startNode)?
                        startPosition:
                        getPosition(content, endNode);

      content.removeSpan(comment);
      if (startNode != null) content.removeSpan(startNode);
      if (endNode != null) content.removeSpan(endNode);

      Editable text = new SpannableStringBuilder();
      addComment(text, comment);
      if (text.length() == 0) return;

      CommentSpan span = new CommentSpan(text);
      content.setSpan(span, startPosition, endPosition, content.SPAN_EXCLUSIVE_EXCLUSIVE);

      span.setAuthor(comment.getAuthor());
      span.setInitials(comment.getInitial());
      span.setTimestamp(comment.getDateTime());
    }

    private final void addComments (Editable content) throws Exception {
      for (CommentDescriptor descriptor : commentDescriptors.values()) {
        addComment(content, descriptor);
      }
    }

    public ContentReader (InputStream stream, Editable content) throws Exception {
      LoadOptions options = new LoadOptions();
      options.setLoadFormat(loadFormat);

      Document document = new Document(stream, options);
      document.updateListLabels();

      mapRevisions(document);
      addSection(content, document.getFirstSection());
      addComments(content);
    }
  }

  @Override
  public final void read (InputStream stream, Editable content) throws IOException {
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
    private final Document document = new Document();
    private final DocumentBuilder builder = new DocumentBuilder(document);

    private abstract class RevisionFinisher {
      public abstract void finishRevision () throws Exception;
    }

    private final void beginRevisionTracking (RevisionSpan span) {
      document.startTrackRevisions(
        span.getAuthor(), span.getTimestamp()
      );
    }

    private final void endRevisionTracking () {
      document.stopTrackRevisions();
    }

    private int bookmarkNumber = 0;

    private final String newBookmarkName () {
      return "bookmark" + ++bookmarkNumber;
    }

    private final BookmarkStart beginBookmark (String name) throws Exception {
      return builder.startBookmark(name);
    }

    private final void endBookmark (String name) throws Exception {
      builder.endBookmark(name);
    }

    private final RevisionFinisher beginInsertRevision (final RevisionSpan span) {
      beginRevisionTracking(span);

      return new RevisionFinisher() {
        @Override
        public void finishRevision () {
          endRevisionTracking();
        }
      };
    }

    private final RevisionFinisher beginDeleteRevision (final RevisionSpan span) throws Exception {
      final String bookmarkName = newBookmarkName();
      final BookmarkStart bookmarkStart = beginBookmark(bookmarkName);

      return new RevisionFinisher() {
        @Override
        public void finishRevision () throws Exception {
          endBookmark(bookmarkName);
          beginRevisionTracking(span);
          bookmarkStart.getBookmark().setText("");
          endRevisionTracking();
        }
      };
    }

    private final RevisionFinisher beginRevision (RevisionSpan span) throws Exception {
      if (span == null) return null;
      if (span instanceof InsertSpan) return beginInsertRevision(span);
      if (span instanceof DeleteSpan) return beginDeleteRevision(span);
      return null;
    }

    private final void writeText (Spanned text) throws Exception {
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
          revisionFinisher = beginRevision(newRevisionSpan);
        }

        if (!isDecoration) {
          builder.write(text.subSequence(start, end).toString());
        }

        start = end;
      }

      if (revisionFinisher != null) revisionFinisher.finishRevision();
    }

    public ContentWriter (OutputStream stream, CharSequence content) throws Exception {
      Spanned text = (content instanceof Spanned)? (Spanned)content: new SpannedString(content);
      writeText(text);
      document.save(stream, saveFormat);
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

package org.nbp.editor;

import java.util.Date;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

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
    super(true);

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

  private class DocumentReader {
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
          span.setName(revision.getAuthor());
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

      if (run.isDeleteRevision()) {
        addRevisionSpan(content, start, new DeleteSpan(), run);
      } else if (run.isInsertRevision()) {
        addRevisionSpan(content, start, new InsertSpan(), run);
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

      if (paragraph.isDeleteRevision()) {
        addRevisionSpan(content, start, new DeleteSpan(), paragraph);
      } else if (paragraph.isInsertRevision()) {
        addRevisionSpan(content, start, new InsertSpan(), paragraph);
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
      span.setName(comment.getAuthor());
      span.setInitials(comment.getInitial());
      span.setTimestamp(comment.getDateTime());

      content.setSpan(
        span, startPosition, endPosition, 
        (startPosition == endPosition)?
          Spanned.SPAN_POINT_POINT:
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      );
    }

    private final void addComments (Editable content) throws Exception {
      for (CommentDescriptor descriptor : commentDescriptors.values()) {
        addComment(content, descriptor);
      }
    }

    public DocumentReader (InputStream stream, Editable content) throws Exception {
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
      new DocumentReader(stream, content);
    } catch (Exception exception) {
      Log.w(LOG_TAG, ("input error: " + exception.getMessage()));
      throw new IOException("Aspose Words input error", exception);
    }
  }

  private class DocumentWriter {
    private final Document document = new Document();
    private final DocumentBuilder builder = new DocumentBuilder(document);
    private int bookmarkNumber = 0;

    private final String newBookmarkName () {
      return "bookmark" + ++bookmarkNumber;
    }

    private final void beginRevisionTracking (RevisionSpan span) {
      document.startTrackRevisions(
        span.getName(), span.getTimestamp()
      );
    }

    private final void endRevisionTracking () {
      document.stopTrackRevisions();
    }

    private final void appendSibling (Node node) {
      builder.getCurrentParagraph().appendChild(node);
    }

    private final void appendSibling (Node node, Node reference) {
      reference.getParentNode().insertAfter(node, reference);
    }

    private class TextWriter {
      private int documentPosition = 0;

      private final void writeText (CharSequence text) throws Exception {
        builder.write(text.toString());
        documentPosition += text.length();
      }

      private abstract class SpanFinisher {
        public abstract void finishSpan () throws Exception;
      }

      private final Map<Integer, List<SpanFinisher>> spanFinishers =
            new HashMap<Integer, List<SpanFinisher>>();

      private final List<SpanFinisher> getSpanFinishers (int position) {
        return spanFinishers.get(position);
      }

      private final void addSpanFinisher (int position, SpanFinisher finisher) {
        if (finisher == null) return;
        List<SpanFinisher> finishers = getSpanFinishers(position);

        if (finishers == null) {
          finishers = new ArrayList();
          spanFinishers.put(position, finishers);
        }

        finishers.add(finisher);
      }

      private final void finishSpans (int position) throws Exception {
        List<SpanFinisher> finishers = getSpanFinishers(position);

        if (finishers != null) {
          for (SpanFinisher finisher : finishers) {
            finisher.finishSpan();
          }

          spanFinishers.remove(position);
        }
      }

      private final SpanFinisher beginInsertRevision (final RevisionSpan span) {
        beginRevisionTracking(span);

        return new SpanFinisher() {
          @Override
          public void finishSpan () {
            endRevisionTracking();
          }
        };
      }

      private final SpanFinisher beginDeleteRevision (final RevisionSpan span) throws Exception {
        final String bookmarkName = newBookmarkName();
        final BookmarkStart bookmarkStart = builder.startBookmark(bookmarkName);

        return new SpanFinisher() {
          @Override
          public void finishSpan () throws Exception {
            builder.endBookmark(bookmarkName);
            Bookmark bookmark = bookmarkStart.getBookmark();

            beginRevisionTracking(span);
            bookmark.setText("");
            endRevisionTracking();

            bookmark.remove();
          }
        };
      }

      private final SpanFinisher beginRevision (RevisionSpan span) throws Exception {
        if (span == null) return null;
        if (span instanceof InsertSpan) return beginInsertRevision(span);
        if (span instanceof DeleteSpan) return beginDeleteRevision(span);
        return null;
      }

      private final SpanFinisher beginComment (final CommentSpan span) throws Exception {
        final int position = documentPosition;

        final Comment comment = new Comment(document);
        appendSibling(comment);

        final Paragraph paragraph = new Paragraph(document);
        comment.getParagraphs().add(paragraph);

        {
          String author = span.getName();
          if (author != null) comment.setAuthor(author);
        }

        {
          String initials = span.getInitials();
          if (initials != null) comment.setInitial(initials);
        }

        {
          Date timestamp = span.getTimestamp();
          if (timestamp != null) comment.setDateTime(timestamp);
        }

        return new SpanFinisher() {
          @Override
          public void finishSpan () throws Exception {
            if (documentPosition != position) {
              int identifier = comment.getId();

              CommentRangeStart start = new CommentRangeStart(document, identifier);
              appendSibling(start, comment);

              CommentRangeEnd end = new CommentRangeEnd(document, identifier);
              appendSibling(end);
            }

            {
              String name = "comment";
              BookmarkStart start = builder.startBookmark(name);
              BookmarkEnd end = builder.endBookmark(name);

              builder.moveTo(paragraph);
              new TextWriter(span.getCommentText());

              builder.moveToBookmark(name);
              start.getBookmark().remove();
            }
          }
        };
      }

      public TextWriter (Spanned text) throws Exception {
        int length = text.length();
        int start = 0;

        while (start < length) {
          int end = text.nextSpanTransition(start, length, Object.class);
          finishSpans(end);

          Font font = builder.getFont();
          font.clearFormatting();

          boolean isDecoration = false;
          Object[] spans = text.getSpans(start, end, Object.class);

          if (spans != null) {
            for (Object span : spans) {
              boolean isStart = text.getSpanStart(span) == start;
              boolean isEnd = text.getSpanEnd(span) == end;

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
              } else if (span instanceof DecorationSpan) {
                isDecoration = true;
              } else if (span instanceof RevisionSpan) {
                if (isStart) {
                  addSpanFinisher(end, beginRevision((RevisionSpan)span));
                }
              } else if (span instanceof CommentSpan) {
                if (isStart) {
                  addSpanFinisher(end, beginComment((CommentSpan)span));
                }
              }
            }
          }

          if (!isDecoration) {
            writeText(text.subSequence(start, end));
          }

          finishSpans(start);
          start = end;
        }
      }
    }

    public DocumentWriter (OutputStream stream, CharSequence content) throws Exception {
      Spanned text = (content instanceof Spanned)? (Spanned)content: new SpannedString(content);
      new TextWriter(text);
      document.save(stream, saveFormat);
    }
  }

  @Override
  public final void write (OutputStream stream, CharSequence content) throws IOException {
    asposeLicense.check();
    if (saveFormat == SaveFormat.UNKNOWN) writingNotSupported();

    try {
      new DocumentWriter(stream, content);
    } catch (Exception exception) {
      Log.w(LOG_TAG, ("output error: " + exception.getMessage()));
      throw new IOException("Aspose Words output error", exception);
    }
  }
}

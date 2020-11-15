package org.nbp.editor.menu.comments;
import org.nbp.editor.*;
import org.nbp.editor.spans.CommentSpan;

public abstract class CommentAction extends SpanAction {
  protected CommentAction (EditorActivity editor) {
    super(editor);
  }

  protected final CommentSpan getCommentSpan () {
    return getSpan(CommentSpan.class);
  }
}

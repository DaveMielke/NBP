package org.nbp.editor;

public abstract class CommentAction extends SpanAction {
  protected CommentAction (EditorActivity editor) {
    super(editor);
  }

  protected final CommentSpan getCommentSpan () {
    return getSpan(CommentSpan.class);
  }
}

package org.nbp.editor;

public abstract class EditorComponent {
  private final EditorActivity editorActivity;

  protected EditorComponent (EditorActivity editor) {
    editorActivity = editor;
  }

  protected final EditorActivity getEditor () {
    return editorActivity;
  }

  protected final EditArea getEditArea () {
    return getEditor().getEditArea();
  }

  protected final void postAction (Runnable action) {
    getEditArea().post(action);
  }
}
